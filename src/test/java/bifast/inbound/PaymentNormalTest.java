package bifast.inbound;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import bifast.inbound.iso20022.AppHeaderService;
import bifast.inbound.isoservice.Pacs008MessageService;
import bifast.inbound.isoservice.Pacs008Seed;
import bifast.inbound.isoservice.SettlementHeaderService;
import bifast.inbound.isoservice.SettlementMessageService;
import bifast.inbound.model.CreditTransfer;
import bifast.inbound.repository.CreditTransferRepository;
import bifast.inbound.service.FlattenIsoMessageService;
import bifast.library.iso20022.custom.BusinessMessage;
import bifast.library.iso20022.custom.Document;
import bifast.library.iso20022.head001.BusinessApplicationHeaderV01;

@CamelSpringBootTest
@EnableAutoConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class PaymentNormalTest {

	@Autowired FlattenIsoMessageService flatMsgService;
	@Autowired ProducerTemplate producerTemplate;
	@Autowired TestUtilService testUtilService;
	@Autowired AppHeaderService appHeaderService;
	@Autowired private Pacs008MessageService pacs008MessageService;
	@Autowired private CreditTransferRepository ctRepo;
	@Autowired private SettlementHeaderService sttlHeaderService;
	@Autowired private SettlementMessageService sttlBodyService;

	@Test
    @Order(1)    
	public void postAE() throws Exception {
		BusinessMessage aeReq = aeRequest();
		String strAEReq = testUtilService.serializeBusinessMessage(aeReq);

		Object ret = producerTemplate.sendBody("direct:receive", ExchangePattern.InOut, strAEReq);
		BusinessMessage bm = testUtilService.deSerializeBusinessMessage((String) ret);

		Assertions.assertInstanceOf(BusinessMessage.class, bm);
		Assertions.assertNotNull(bm.getDocument().getFiToFIPmtStsRpt());
		Assertions.assertEquals(bm.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getTxSts(), "ACTC");
	}

	static final BusinessMessage ctReq = new BusinessMessage();
	private static String endToEndId = null;

	@Test
    @Order(2)    
	public void postCT() throws Exception {
		BusinessMessage newCT = buildCTRequest();
		ctReq.setAppHdr(newCT.getAppHdr());
		ctReq.setDocument(newCT.getDocument());
		endToEndId = ctReq.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getEndToEndId();
		
		String strCTReq = testUtilService.serializeBusinessMessage(ctReq);
		Object ret = producerTemplate.sendBody("direct:receive", ExchangePattern.InOut, strCTReq);
		BusinessMessage bm = testUtilService.deSerializeBusinessMessage((String) ret);

		TimeUnit.SECONDS.sleep(1);
		List<CreditTransfer> lCt = ctRepo.findAllByEndToEndId(endToEndId);
		CreditTransfer ct = null;
		if (lCt.size()>0) ct = lCt.get(0);

		Assertions.assertNotNull(bm.getDocument().getFiToFIPmtStsRpt());
		Assertions.assertEquals(bm.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getTxSts(), "ACTC");
		Assertions.assertNotNull(ct);
		Assertions.assertEquals(ct.getCallStatus(), "SUCCESS");
		Assertions.assertEquals(ct.getCbStatus(), "PENDING");
		Assertions.assertEquals(ct.getSettlementConfBizMsgIdr(), "WAITING");
		
	}

	@Test
    @Order(3)    
	public void postSttl() throws Exception {
		String bizMsgId = testUtilService.genRfiBusMsgId("010", "02", "INDOIDJA");
		String msgId = testUtilService.genMessageId("010", "INDOIDJA");

		BusinessMessage settlementConf = new BusinessMessage();

		settlementConf.setAppHdr(sttlHeaderService.getAppHdr("010", bizMsgId));
		settlementConf.setDocument(new Document());
		settlementConf.getDocument().setFiToFIPmtStsRpt(sttlBodyService.SettlementConfirmation(msgId, ctReq));
		String strSettl = testUtilService.serializeBusinessMessage(settlementConf);

		producerTemplate.sendBody("direct:receive", strSettl);

		List<CreditTransfer> lCt = ctRepo.findAllByEndToEndId(endToEndId);
		CreditTransfer ct = null;
		if (lCt.size()>0) ct = lCt.get(0);

		Assertions.assertNotNull(ct);
		Assertions.assertEquals(ct.getSettlementConfBizMsgIdr(), "RECEIVED");
		
		CreditTransfer ct2 = null;
		int ctr = 0;
		boolean found = false;
		while (!found && ctr < 20) {
			ctr = ctr+1;
			TimeUnit.SECONDS.sleep(3);
			ct2 = ctRepo.findById(lCt.get(0).getId()).orElse(null);
			if (ct2.getCbStatus().equals("DONE")) found = true;
		}
		Assertions.assertEquals(ct2.getCbStatus(), "DONE");

	}
	
	private BusinessMessage aeRequest() throws Exception  {
		String bizMsgId = testUtilService.genRfiBusMsgId("510", "01", "BMNDIDJA");
		String msgId = testUtilService.genMessageId("510", "BMNDIDJA");
		BusinessApplicationHeaderV01 hdr = new BusinessApplicationHeaderV01();
		hdr = appHeaderService.getAppHdr("pacs.008.001.08", bizMsgId);
		Pacs008Seed seedAcctEnquiry = new Pacs008Seed();
		seedAcctEnquiry.setMsgId(msgId);
		seedAcctEnquiry.setBizMsgId(hdr.getBizMsgIdr());
		seedAcctEnquiry.setAmount(new BigDecimal(100000));
		seedAcctEnquiry.setCategoryPurpose("01");
		seedAcctEnquiry.setCrdtAccountNo("3604107554096");
		seedAcctEnquiry.setOrignBank("BMNDIDJA");
		seedAcctEnquiry.setRecptBank("SIHBIDJ1");
		seedAcctEnquiry.setTrnType("510");
		seedAcctEnquiry.setPaymentInfo("");

		Document doc = new Document();
		doc.setFiToFICstmrCdtTrf(pacs008MessageService.accountEnquiryRequest(seedAcctEnquiry));

		BusinessMessage busMsg = new BusinessMessage();
		busMsg.setAppHdr(hdr);
		busMsg.setDocument(doc);
		return busMsg;
	}
	
	private BusinessMessage buildCTRequest() throws Exception {
		Pacs008Seed seedCreditTrn = new Pacs008Seed();
		String bizMsgId = testUtilService.genRfiBusMsgId("010", "01", "BMNDIDJA" );
		String msgId = testUtilService.genMessageId("010", "BMNDIDJA");
		seedCreditTrn.setBizMsgId(bizMsgId);
		seedCreditTrn.setMsgId(msgId);
		seedCreditTrn.setAmount(new BigDecimal(100000));
		seedCreditTrn.setCategoryPurpose("01");
		seedCreditTrn.setChannel("01");
		seedCreditTrn.setCrdtAccountNo("3604107554096");		
		seedCreditTrn.setCrdtAccountType("CACC");
		seedCreditTrn.setCrdtName("Johari");
		seedCreditTrn.setDbtrAccountNo("2001000");
		seedCreditTrn.setDbtrAccountType("SVGS");
		seedCreditTrn.setDbtrName("Antonio");
		seedCreditTrn.setDbtrId("9999333339");
		seedCreditTrn.setDbtrType("01"); 
		seedCreditTrn.setDbtrResidentStatus("01");
		seedCreditTrn.setDbtrTownName("0300");
		seedCreditTrn.setOrignBank("BMNDIDJA");
		seedCreditTrn.setRecptBank("SIHBIDJ1");
		seedCreditTrn.setPaymentInfo("");
		seedCreditTrn.setTrnType("010");
		
		BusinessMessage busMsg = new BusinessMessage();
		BusinessApplicationHeaderV01 hdr = new BusinessApplicationHeaderV01();
		hdr = appHeaderService.getAppHdr("pacs.008.001.08", bizMsgId);
		busMsg.setAppHdr(hdr);
		Document doc = new Document();
		doc.setFiToFICstmrCdtTrf(pacs008MessageService.creditTransferRequest(seedCreditTrn));
		busMsg.setDocument(doc);
		return busMsg;
	}


}
