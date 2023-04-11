package bifast.inbound.settlement;

import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.config.Config;
import bifast.inbound.model.CreditTransfer;
import bifast.inbound.repository.CreditTransferRepository;

@Component
public class SettlementRoute extends RouteBuilder {
	@Autowired private CreditTransferRepository ctRepo;
	@Autowired private Config config;
	@Autowired private SaveSettlementMessageProcessor saveSettlement;
	@Autowired private SettlementDebitProc settlementDebitProcessor;

	private static Logger logger = LoggerFactory.getLogger(SettlementRoute.class);

	@Override
	public void configure() throws Exception {

		from("direct:settlement").routeId("komi.settlement")
			
			// prepare untuk request ke corebank

	 		.log(LoggingLevel.DEBUG, "komi.settlement", 
	 				"[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] Terima settlement")

	 		.process(new Processor () {
				public void process(Exchange exchange) throws Exception {
					String e2eid = exchange.getProperty("end2endid", String.class);
					Optional<CreditTransfer> oOrgnlCT = ctRepo.getSuccessByEndToEndId(e2eid);
					String settlment_ctType = "";

					if (oOrgnlCT.isPresent()) {
						CreditTransfer orgnlCT = oOrgnlCT.get();
						exchange.setProperty("pr_orgnlCT", orgnlCT);
						logger.debug("[Settl:" +e2eid+ "] OrgnlCT.req_bizmsgidr: " + orgnlCT.getCrdtTrnRequestBizMsgIdr());
						
						if (orgnlCT.getOriginatingBank().equals(config.getBankcode())) 
							settlment_ctType = "Outbound";
						else
							settlment_ctType = "Inbound";
						
						logger.debug("[Settl:" + e2eid + "] Settlement for " + settlment_ctType + " message");

					}
					else
						logger.warn("[Settl:" + e2eid + "] Original payment NOT FOUND.");
						
					exchange.setProperty("pr_sttlType", settlment_ctType);

				}
	 		})
			
	 		.process(saveSettlement)

	 		.filter().simple("${exchangeProperty.pr_sttlType} == 'Outbound'")
				.process(settlementDebitProcessor)
				.to("direct:isoadpt-sttl")
		 		.log("[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] Selesai posting settlement")
	 		.end()
	 		
		;

	}

}
