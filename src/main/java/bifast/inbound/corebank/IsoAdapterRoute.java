package bifast.inbound.corebank;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import bifast.inbound.corebank.isopojo.AccountEnquiryRequest;
import bifast.inbound.corebank.isopojo.AccountEnquiryResponse;
import bifast.inbound.corebank.isopojo.CreditRequest;
import bifast.inbound.corebank.isopojo.CreditResponse;
import bifast.inbound.corebank.isopojo.DebitReversalRequest;
import bifast.inbound.corebank.isopojo.DebitReversalResponse;
import bifast.inbound.corebank.processor.CbCallFaultProcessor;
import bifast.inbound.corebank.processor.SaveCBTransactionProc;
import bifast.inbound.pojo.FaultPojo;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.processor.EnrichmentAggregator;
import bifast.inbound.service.JacksonDataFormatService;

@Component
public class IsoAdapterRoute extends RouteBuilder{
	@Autowired private JacksonDataFormatService jdfService;
	@Autowired private EnrichmentAggregator enrichmentAggregator;
	@Autowired private CbCallFaultProcessor cbFaultProcessor;
	@Autowired private SaveCBTransactionProc saveCBTransactionProc;

	@Override
	public void configure() throws Exception {
		JacksonDataFormat aeRequestJDF = jdfService.basic(AccountEnquiryRequest.class);
		JacksonDataFormat aeResponseJDF = jdfService.basic(AccountEnquiryResponse.class);
		JacksonDataFormat debitReversalReqJDF = jdfService.basic(DebitReversalRequest.class);
		JacksonDataFormat debitReversalResponseJDF = jdfService.basic(DebitReversalResponse.class);

		
		from("direct:isoadpt").routeId("komi.isoadapter")

			.removeHeaders("*")

			.setHeader("cb_msgname", simple("${exchangeProperty[prop_process_data.inbMsgName]}"))
			.setHeader("cb_e2eid", simple("${exchangeProperty[prop_process_data.endToEndId]}"))

			.setProperty("cb_request", simple("${body}"))
			
			.log(LoggingLevel.DEBUG,"komi.isoadapter", "[${header.cb_msgname}:${header.cb_e2eid}] Terima di corebank: ${body}")
					
			.choice()
				.when().simple("${body.class} endsWith 'AccountEnquiryInboundRequest'")
					.setHeader("cb_requestName", constant("accountenquiry"))
					.setHeader("cb_url", simple("{{komi.url.isoadapter.accountinquiry}}"))
					.marshal(aeRequestJDF)
				.when().simple("${body.class} endsWith 'DebitReversalRequest'")
					.setHeader("cb_requestName", constant("debitreversal"))
					.setHeader("cb_url", simple("{{komi.url.isoadapter.credit}}"))
					.marshal(debitReversalReqJDF)

				.otherwise()
		 			.log("Akan kirim : ${body.class}")
			.end()
			
			.setProperty("cb_request_str", simple("${body}"))
			
	 		.log("[${header.cb_msgname}:${header.cb_e2eid}] POST ${header.cb_url}")
	 		.log("[${header.cb_msgname}:${header.cb_e2eid}] CB Request: ${body}")
			
	 		.doTry()
				.setHeader("HttpMethod", constant("POST"))
				.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
				.enrich().simple("${header.cb_url}?bridgeEndpoint=true")
					
				.aggregationStrategy(enrichmentAggregator)
				.convertBodyTo(String.class)
				
				.log("[${header.cb_msgname}:${header.cb_e2eid}] CB response: ${body}")

		    	.choice()
		 			.when().simple("${header.cb_requestName} == 'accountenquiry'")
		 				.unmarshal(aeResponseJDF)
	 				.endChoice()
		 			.when().simple("${header.cb_requestName} == 'debitreversal'")
	 					.unmarshal(debitReversalResponseJDF)
	 				.endChoice()
		 		.end()

 				.setHeader("cb_response", simple("${body.status}"))
 				.setHeader("cb_reason", simple("${body.reason}"))

				.filter().simple("${header.cb_response} != 'ACTC' ")
					.process(new Processor() {
						public void process(Exchange exchange) throws Exception {
							FaultPojo fault = new FaultPojo();
							fault.setCallStatus("SUCCESS");
							String cbResponse = exchange.getMessage().getHeader("cb_response", String.class);
							String cbReason = exchange.getMessage().getHeader("cb_reason", String.class);
							fault.setResponseCode(cbResponse);
							fault.setReasonCode(cbReason);
							exchange.getMessage().setBody(fault);
							}
						})
				.end()
				
	 		.endDoTry()
	    	.doCatch(HttpOperationFailedException.class).onWhen(simple("${exception.statusCode} == '504'"))
	    		.log(LoggingLevel.ERROR, "[${header.cb_msgname}:${header.cb_e2eid}] Corebank TIMEOUT: \n ${exception.message}")
	    		.process(cbFaultProcessor)
	    	.doCatch(Exception.class)
				.log(LoggingLevel.ERROR, "[${header.cb_msgname}:${header.cb_e2eid}] Call CB Error.")
		    	.log(LoggingLevel.ERROR, "${exception.stacktrace}")
		    	.process(cbFaultProcessor)
	    	.end()

			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
					Object cbResponse = exchange.getMessage().getBody(Object.class);
					processData.setCorebankResponse(cbResponse);
//					exchange.setProperty("prop_process_data", processData);
				}
			})
			
			.filter().simple("${header.cb_requestName} in 'credit,settlement,debitreversal'")
				.process(saveCBTransactionProc)
			.end()
			
			.removeHeaders("cb_*")
		;
		
	}

}
