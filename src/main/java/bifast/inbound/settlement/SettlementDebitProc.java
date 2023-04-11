package bifast.inbound.settlement;

import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import bifast.inbound.config.Config;
import bifast.inbound.corebank.isopojo.SettlementRequest;
import bifast.inbound.model.ChannelTransaction;
import bifast.inbound.model.CreditTransfer;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs002Pojo;
import bifast.inbound.repository.ChannelTransactionRepository;
import bifast.inbound.service.RefUtils;

@Component
public class SettlementDebitProc implements Processor {
	@Autowired private ChannelTransactionRepository chnlTrnsRepo;
	@Autowired private Config config;

	@Value("${komi.isoadapter.merchant}")
	String merchant;

	@Value("${komi.isoadapter.terminal}")
	String terminal;

	@Value("${komi.isoadapter.txid}")
	String txid;

	private static Logger logger = LoggerFactory.getLogger(SettlementDebitProc.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
		FlatPacs002Pojo flatSttl = (FlatPacs002Pojo) processData.getBiRequestFlat();

		CreditTransfer ct = exchange.getProperty("pr_orgnlCT", CreditTransfer.class);
		logger.debug("[Settl:" + processData.getEndToEndId() + "] OrgnlCT.req_bizmsgidr: " + ct.getCrdtTrnRequestBizMsgIdr());

		SettlementRequest sttlRequest = new SettlementRequest();

		RefUtils.Ref ref = RefUtils.newRef();

		sttlRequest.setAdditionalInfo(flatSttl.getRsnInfAddtlInf());
		sttlRequest.setDateTime(ref.getDateTime());
		sttlRequest.setMerchantType(merchant);
		sttlRequest.setNoRef(ref.getNoRef());
		
		sttlRequest.setReason("U000");
		sttlRequest.setStatus("ACSC");
		sttlRequest.setTerminalId(terminal);
		sttlRequest.setTransactionId(txid);

		sttlRequest.setBizMsgId(ct.getCrdtTrnRequestBizMsgIdr());
		sttlRequest.setMsgId(flatSttl.getOrgnlEndToEndId());
		
		if (ct.getOriginatingBank().equals(config.getBankcode()))
			sttlRequest.setCounterParty(ct.getRecipientBank());
		else
			sttlRequest.setCounterParty(ct.getOriginatingBank());

//		Optional<ChannelTransaction> oChnlTrns = chnlTrnsRepo.findByKomiTrnsId(ct.getKomiTrnsId());
		Optional<ChannelTransaction> oChnlTrns =chnlTrnsRepo.findById(ct.getKomiTrnsId());
		if (oChnlTrns.isPresent())
			sttlRequest.setOriginalNoRef(oChnlTrns.get().getChannelRefId());
				
		exchange.getMessage().setBody(sttlRequest);

				
	}

}
