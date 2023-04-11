package bifast.inbound.settlement;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import bifast.inbound.corebank.isopojo.SettlementRequest;
import bifast.inbound.corebank.isopojo.SettlementResponse;
import bifast.inbound.corebank.processor.CbCallFaultProcessor;
import bifast.inbound.pojo.FaultPojo;
import bifast.inbound.processor.EnrichmentAggregator;
import bifast.inbound.service.JacksonDataFormatService;


@Component
public class CbSettlementRoute extends RouteBuilder{
	@Autowired private JacksonDataFormatService jdfService;
	@Autowired private EnrichmentAggregator enrichmentAggregator;
	@Autowired private CbCallFaultProcessor cbFaultProcessor;

	@Override
	public void configure() throws Exception {
		JacksonDataFormat settlementJDF = jdfService.basic(SettlementRequest.class);
		JacksonDataFormat settlementResponseJDF = jdfService.basic(SettlementResponse.class);

		// ROUTE CALLCB 
		from("direct:isoadpt-sttl").routeId("komi.isoapt.settlement")
			.removeHeaders("*")
			.setProperty("cb_request", simple("${body}"))
			.marshal(settlementJDF)
			.log("Settlement body: ${body}")

			.setProperty("cb_request_str", simple("${body}"))
			
	 		.log(LoggingLevel.DEBUG, "komi.isoapt.settlement", 
	 				"[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] "
	 				+ "POST {{komi.url.isoadapter.settlement}}")
	 		.log("[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] CB Request: ${body}")
			
	 		.doTry()
				.setHeader("HttpMethod", constant("POST"))
				.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
				.enrich().simple("{{komi.url.isoadapter.settlement}}?bridgeEndpoint=true")
					
				.aggregationStrategy(enrichmentAggregator)
				.convertBodyTo(String.class)
				
				.log("[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] CB response: ${body}")

				.unmarshal(settlementResponseJDF)

 				.setHeader("cb_response", simple("${body.status}"))
 				.setHeader("cb_reason", simple("${body.reason}"))

				.filter().simple("${header.cb_response} != 'ACTC' ")
					.process(new Processor() {
						public void process(Exchange exchange) throws Exception {
							FaultPojo fault = new FaultPojo();
							String cbResponse = exchange.getMessage().getHeader("cb_response", String.class);
							String cbReason = exchange.getMessage().getHeader("cb_reason", String.class);
							fault.setResponseCode(cbResponse);
							fault.setReasonCode(cbReason);
							exchange.getMessage().setBody(fault);
							}
						})
				.end()
				
	 		.endDoTry()
	    	.doCatch(Exception.class)
				.log(LoggingLevel.ERROR, "[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] Call CB Error.")
		    	.log(LoggingLevel.ERROR, "${exception.stacktrace}")
		    	.process(cbFaultProcessor)
	    	.end()

		;
		
		
	}

}
