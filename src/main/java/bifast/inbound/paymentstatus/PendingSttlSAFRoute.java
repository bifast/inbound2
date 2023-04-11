package bifast.inbound.paymentstatus;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.processor.EnrichmentAggregator;
import bifast.inbound.service.JacksonDataFormatService;
import bifast.library.iso20022.custom.BusinessMessage;

@Component
public class PendingSttlSAFRoute extends RouteBuilder {
	@Autowired private EnrichmentAggregator enrichmentAggr;
	@Autowired private BuildPSSAFRequestProcessor buildPSRequest;;
	@Autowired private JacksonDataFormatService jdfService;
	@Autowired private ProcessQuerySAFProcessor processQueryProcessor;
	@Autowired private PSFilter psFilter;
	@Autowired private UpdatePendingCTProc updatePendingCT;

	@Override
	public void configure() throws Exception {
		
		JacksonDataFormat businessMessageJDF = jdfService.wrapUnwrapRoot(BusinessMessage.class);
		
		from("sql:select ct.id, "
				+ "ct.req_bizmsgid, "
				+ "ct.komi_trns_id as komi_id, "
				+ "ct.full_request_msg as txtctreq, "
				+ "ct.e2e_id, "
				+ "ct.recpt_bank, "
				+ "ct.last_update_dt, "
				+ "ct.ps_counter "
				+ "from kc_credit_transfer ct "
				+ "where ct.cb_status = 'PENDING' "
				+ "and ct.sttl_bizmsgid = 'WAITING' "
				+ "and ct.ps_counter < 3 "
				+ "limit 20"
				+ "?delay=60000"
//				+ "&sendEmptyMessageWhenIdle=true"
				)
			.routeId("komi.inbct.saf")
						
			.process(processQueryProcessor)
			.filter().method(psFilter, "timeIsDue")		
			
			.log("[PymSts:${exchangeProperty.pr_psrequest.endToEndId}] Retry ${exchangeProperty.pr_psrequest.psCounter}")
		
			.process(buildPSRequest)

//			.to("direct:call-cihub")				
			.marshal(businessMessageJDF)
			
			.log("[PymSts:${exchangeProperty.pr_psrequest.endToEndId}] Cihub request: ${body}")
			
			.doTry()
				.setHeader("HttpMethod", constant("POST"))
				.enrich()
					.simple("{{komi.url.ciconnector}}?"
						+ "socketTimeout=5000&" 
						+ "bridgeEndpoint=true")
					.aggregationStrategy(enrichmentAggr)
				
				.convertBodyTo(String.class)				
				
				.log("[PymSts:${exchangeProperty.pr_psrequest.endToEndId}] CIHUB response: ${body}")
	
				.unmarshal(businessMessageJDF)

				.process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						BusinessMessage bm = exchange.getMessage().getBody(BusinessMessage.class);
						String response = bm.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getTxSts();
						String reason = bm.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getStsRsnInf().get(0).getRsn().getPrtry();
						exchange.setProperty("pr_psresponse", response);
						exchange.setProperty("pr_psreason", reason);
					}
				})
			
			.endDoTry()
			.doCatch(java.net.SocketTimeoutException.class)
				.log(LoggingLevel.ERROR, "[PymSts:${exchangeProperty.pr_psrequest.endToEndId}] CI-HUB TIMEOUT.")
				.setProperty("pr_psresponse", constant("TIMEOUT"))
				.setProperty("pr_psreason", constant("U900"))
			.doCatch(Exception.class)
				.log(LoggingLevel.ERROR, "[$PymSts:${exchangeProperty.pr_psrequest.endToEndId}] Call CI-HUB Error.")
		    	.log(LoggingLevel.ERROR, "${exception.stacktrace}")
				.setProperty("pr_psresponse", constant("ERROR"))
				.setProperty("pr_psresponse", constant(""))
//		    	.process(exceptionToFaultMap)
			.end()

			.process(updatePendingCT) 

			// kalo terima settlement, forward ke Inbound Service
			.filter().simple("${exchangeProperty.pr_psresponse} == 'ACSC'")
				.log(LoggingLevel.DEBUG, "komi.inbct.saf", 
						"[PymSts:${exchangeProperty.pr_psrequest.endToEndId}] Settlement dari CIHUB kirim ke Inbound Service")
				.marshal(businessMessageJDF)
				.to("direct:receive")
			.end()

			.removeProperties("pr_*")
					
		;


	}

}
