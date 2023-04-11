package bifast.inbound.credittransfer.processor;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import bifast.inbound.config.Config;
import bifast.inbound.corebank.isopojo.SettlementRequest;
import bifast.inbound.model.CorebankTransaction;
import bifast.inbound.model.Settlement;
import bifast.inbound.pojo.flat.FlatPacs002Pojo;
import bifast.inbound.repository.CorebankTransactionRepository;
import bifast.inbound.repository.SettlementRepository;
import bifast.inbound.service.CallRouteService;
import bifast.inbound.service.FlattenIsoMessageService;
import bifast.inbound.service.RefUtils;
import bifast.library.iso20022.custom.BusinessMessage;

@Component
public class CbSettlementRequestProc implements Processor {
	@Autowired private CallRouteService routeService;
	@Autowired private Config config;
	@Autowired private CorebankTransactionRepository cbRepo;
	@Autowired private FlattenIsoMessageService flatMessageService;
	@Autowired private SettlementRepository settlementRepo;

	@Value("${komi.isoadapter.merchant}")
	String merchant;

	@Value("${komi.isoadapter.terminal}")
	String terminal;

	@Value("${komi.isoadapter.txid}")
	String txid;

//	private static Logger logger = LoggerFactory.getLogger(SettlementProcessor.class);


	@Override
	public void process(Exchange exchange) throws Exception {
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> arr = exchange.getProperty("ctsaf_qryresult",HashMap.class);
		String komiTrnsId = String.valueOf(arr.get("komi_trns_id"));
		String e2eid = String.valueOf(arr.get("e2e_id"));

		List<Settlement> setts = settlementRepo.findByOrgnlEndToEndId(e2eid);
		
		BusinessMessage settlementMsg = null;
		
		if (setts.size()>0) {
			settlementMsg = routeService.decryptBusinessMessage(setts.get(0).getFullMessage());
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(settlementMsg));
		}
		FlatPacs002Pojo flatMsg = flatMessageService.flatteningPacs002(settlementMsg);

		SettlementRequest sttlRequest = new SettlementRequest();

		RefUtils.Ref ref = RefUtils.newRef();

		sttlRequest.setAdditionalInfo("");
		sttlRequest.setDateTime(ref.getDateTime());
		sttlRequest.setMerchantType(merchant);
		sttlRequest.setNoRef(ref.getNoRef());
		sttlRequest.setReason("U000");
		sttlRequest.setStatus("ACSC");
		sttlRequest.setTerminalId(terminal);
		sttlRequest.setTransactionId(txid);
		
		sttlRequest.setBizMsgId(flatMsg.getBizMsgIdr());
		sttlRequest.setMsgId(flatMsg.getOrgnlEndToEndId());	
		
		if (flatMsg.getCdtrAgtFinInstnId().equals(config.getBankcode()))
			sttlRequest.setCounterParty(flatMsg.getDbtrAgtFinInstnId());
		else
			sttlRequest.setCounterParty(flatMsg.getCdtrAgtFinInstnId());			
				
		String orgnlNoRef = "";
		List<CorebankTransaction> lCorebankTransaction = cbRepo.findByTransactionTypeAndKomiTrnsId("Credit", komiTrnsId);

		for (CorebankTransaction core : lCorebankTransaction) {
			if (core.getResponse().equals("ACTC")) {
				orgnlNoRef = core.getKomiNoref();
				break;
			}
		}
		
		sttlRequest.setOriginalNoRef(orgnlNoRef);
		
		exchange.getMessage().setBody(sttlRequest);
	}
}
