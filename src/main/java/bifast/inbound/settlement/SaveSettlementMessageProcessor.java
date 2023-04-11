package bifast.inbound.settlement;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.model.CreditTransfer;
import bifast.inbound.model.Settlement;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs002Pojo;
import bifast.inbound.repository.CreditTransferRepository;
import bifast.inbound.repository.SettlementRepository;
import bifast.inbound.service.CallRouteService;

@Component
public class SaveSettlementMessageProcessor implements Processor {
	@Autowired private CreditTransferRepository ctRepo;
	@Autowired private SettlementRepository settlementRepo;
	@Autowired private CallRouteService routeService;

//	private static Logger logger = LoggerFactory.getLogger(SaveSettlementMessageProcessor.class);

	public void process(Exchange exchange) throws Exception {

		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);

		FlatPacs002Pojo flatSttl = (FlatPacs002Pojo) processData.getBiRequestFlat();

		Settlement sttl = new Settlement();
		sttl.setSettlBizMsgId(flatSttl.getBizMsgIdr());
		sttl.setOrgnlEndToEndId(flatSttl.getOrgnlEndToEndId());
		sttl.setReceiveDate(LocalDateTime.now());
		
		sttl.setFullMessage(routeService.encryptBusinessMessage(processData.getBiRequestMsg()));
		
		sttl.setDbtrBank(flatSttl.getDbtrAgtFinInstnId());
		sttl.setCrdtBank(flatSttl.getCdtrAgtFinInstnId());

		if (!(null == flatSttl.getCdtrAcctId()))
			sttl.setCrdtAccountNo(flatSttl.getCdtrAcctId());
		if (!(null == flatSttl.getDbtrAcctId()))
			sttl.setDbtrAccountNo(flatSttl.getDbtrAcctId());

		CreditTransfer ct = null;
		Optional<CreditTransfer> oct = ctRepo.getSuccessByEndToEndId(flatSttl.getOrgnlEndToEndId());
		if (oct.isPresent()) {
			ct = oct.get();
			ct.setSettlementConfBizMsgIdr("RECEIVED");
			ctRepo.save(ct);
			
			sttl.setOrgnlCTBizMsgId(ct.getCrdtTrnRequestBizMsgIdr());
			sttl.setKomiTrnsId(ct.getKomiTrnsId());
			
			CreditTransfer ct2 = ctRepo.getSuccessByEndToEndId(flatSttl.getOrgnlEndToEndId()).orElse(new CreditTransfer());
			if (!(Optional.ofNullable(ct2.getSettlementConfBizMsgIdr()).orElse("").equals("RECEIVED"))) {
				ct.setSettlementConfBizMsgIdr("RECEIVED.");
				ctRepo.save(ct);
			}
		}
		
		settlementRepo.save(sttl);
		
	}
}
