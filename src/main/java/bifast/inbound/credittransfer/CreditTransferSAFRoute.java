package bifast.inbound.credittransfer;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.credittransfer.processor.CTCorebankRequestProcessor;
import bifast.inbound.credittransfer.processor.InitiateCTJobProcessor;
import bifast.inbound.credittransfer.processor.CbSettlementRequestProc;

@Component
public class CreditTransferSAFRoute extends RouteBuilder {
	@Autowired private CTCorebankRequestProcessor ctRequestProcessor;
	@Autowired private CbSettlementRequestProc settlementRequestPrc;
	@Autowired private InitiateCTJobProcessor initCTJobProcessor;
	
	@Override
	public void configure() throws Exception {
		
//		JacksonDataFormat businessMessageJDF = jdfService.wrapUnwrapRoot(BusinessMessage.class);

		from("sql:select kct.id , "
				+ "kct.komi_trns_id, "
				+ "kct.req_bizmsgid, "
				+ "kct.response_code, "
				+ "kct.full_request_msg ct_msg, "
				+ "kct.e2e_id "
				+ "from kc_credit_transfer kct "
				+ "where kct.cb_status = 'PENDING' "
				+ "and kct.sttl_bizmsgid = 'RECEIVED' "
				+ "?delay=8000"
//				+ "&sendEmptyMessageWhenIdle=true"
				)
			.routeId("komi.ct.saf")
					
			.log("mulai")
			.setProperty("ctsaf_qryresult", simple("${body}"))
			.log("[CTSAF:${exchangeProperty.ctsaf_qryresult[e2e_id]}] Submit incoming CreditTransfer started.")
			
			.process(initCTJobProcessor)  // hdr_process_data

			.process(ctRequestProcessor)
			// send ke corebank
			.setHeader("cb_msgname", constant("CTSAF"))
			.to("direct:isoadpt-credit")
			
			.log(LoggingLevel.DEBUG, "komi.ct.saf", "[CTSAF:${exchangeProperty.ctsaf_qryresult[e2e_id]}] Selesai call credit account")
			
			.choice()
				.when().simple("${body.status} == 'TIMEOUT'")
					.log(LoggingLevel.DEBUG,"komi.ct.saf", "[CTSAF:${exchangeProperty.ctsaf_qryresult[e2e_id]}] Update CT status TIMEOUT")
					.to("sql:update kc_credit_transfer "
							+ "set cb_status = 'TIMEOUT' "
							+ "where id = :#${exchangeProperty.ctsaf_qryresult[id]}")
				.endChoice()
				.when().simple("${body.status} == 'RJCT'")
					.to("sql:update kc_credit_transfer "
							+ "set cb_status = 'DONE', reversal = 'PENDING' "
							+ "where id = :#${exchangeProperty.ctsaf_qryresult[id]}")
				.endChoice()
				.when().simple("${body.status} == 'ERROR'")
					.log(LoggingLevel.ERROR, "[CTSAF:${exchangeProperty.ctsaf_qryresult[e2e_id]}] error submit credit-account ke cbs")
					.to("sql:update kc_credit_transfer "
							+ "set cb_status = 'ERROR', reversal = 'PENDING' "
							+ "where id = :#${exchangeProperty.ctsaf_qryresult[id]}")
				.endChoice()
				
				.when().simple("${body.status} == 'ACTC'")
					.log(LoggingLevel.DEBUG,"komi.ct.saf", "[CTSAF:${exchangeProperty.ctsaf_qryresult[e2e_id]}] CT corebank Accepted")
					.setProperty("ctsaf_settlement", constant("YES"))
					.to("sql:update kc_credit_transfer "
							+ "set cb_status = 'DONE' "
							+ "where id = :#${exchangeProperty.ctsaf_qryresult[id]}")

					.log(LoggingLevel.DEBUG,"komi.settlement.inbound", "[CTSAF:${exchangeProperty.ctsaf_qryresult[e2e_id]}] Akan post settlement")
					.process(settlementRequestPrc)
					.to("direct:isoadpt-sttl")

				.endChoice()
			.end()
							
		;

	}

}
