package bifast.inbound.reversecrdttrns.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.iso20022.AppHeaderService;
import bifast.inbound.pojo.Pacs002Seed;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs008Pojo;
import bifast.inbound.service.Pacs002MessageService;
import bifast.inbound.service.UtilService;
import bifast.library.iso20022.custom.BusinessMessage;
import bifast.library.iso20022.custom.Document;
import bifast.library.iso20022.head001.BusinessApplicationHeaderV01;
import bifast.library.iso20022.pacs002.FIToFIPaymentStatusReportV10;

@Component
public class RevCTRejectResponseProcessor implements Processor {
	@Autowired private AppHeaderService appHdrService;
	@Autowired private Pacs002MessageService pacs002Service;
	@Autowired private UtilService utilService;

	@Override
	public void process(Exchange exchange) throws Exception {
		
		
		Pacs002Seed resp = new Pacs002Seed();
		
		FlatPacs008Pojo flatRequest = exchange.getProperty("flatRequest", FlatPacs008Pojo.class);

		resp.setStatus("RJCT");
		resp.setReason("62");				

		String checkRevResult = exchange.getProperty("pr_revCTCheckRsl", String.class);
		if (checkRevResult.equals("DataNotMatch")) 
			resp.setAdditionalInfo("Data tidak sesuai");
		else if (checkRevResult.equals("AccountInActive")) 
			resp.setAdditionalInfo("Account tidak aktif");
		else  // CT Not Found
			resp.setAdditionalInfo("Transaksi asal tidak ditemukan");
				
		resp.setCreditorAccountIdType(flatRequest.getCreditorAccountType());

		resp.setCreditorResidentialStatus(flatRequest.getCreditorResidentialStatus());  // 01 RESIDENT
		resp.setCreditorTown(flatRequest.getCreditorTownName());  
		resp.setCreditorType(flatRequest.getCreditorType());
		if (null != flatRequest.getCreditorId())
			resp.setCreditorId(flatRequest.getCreditorId());
		if (!(null == flatRequest.getCreditorName())) 
			resp.setCreditorName(flatRequest.getCreditorName());


		String komiTrnsId = exchange.getProperty("pr_komitrnsid", String.class);
		String msgId = utilService.genMsgId("011", komiTrnsId);
		resp.setMsgId(msgId);

		String bizMsgId = utilService.genRfiBusMsgId("011", komiTrnsId);
		BusinessApplicationHeaderV01 appHdr = appHdrService.getAppHdr("pacs.002.001.10", bizMsgId);
		appHdr.setBizSvc("CLEAR");

		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);

		FIToFIPaymentStatusReportV10 respMsg = pacs002Service.creditTransferRequestResponse(resp, processData.getBiRequestMsg());
		Document doc = new Document();
		doc.setFiToFIPmtStsRpt(respMsg);

		BusinessMessage respBusMesg = new BusinessMessage();
		respBusMesg.setAppHdr(appHdr);
		respBusMesg.setDocument(doc);

		exchange.getIn().setBody(respBusMesg);
		
		processData.setBiResponseMsg(respBusMesg);
//		exchange.setProperty("prop_process_data", processData);
		
	}
}
