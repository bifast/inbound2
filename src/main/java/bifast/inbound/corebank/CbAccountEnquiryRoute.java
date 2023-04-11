package bifast.inbound.corebank;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import bifast.inbound.corebank.isopojo.AccountEnquiryRequest;
import bifast.inbound.corebank.isopojo.AccountEnquiryResponse;
import bifast.inbound.corebank.processor.CbAEFailedProc;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.processor.EnrichmentAggregator;
import bifast.inbound.service.JacksonDataFormatService;

@Component
public class CbAccountEnquiryRoute extends RouteBuilder{
	@Autowired private CbAEFailedProc cbFaultProcessor;
	@Autowired private EnrichmentAggregator enrichmentAggregator;
	@Autowired private JacksonDataFormatService jdfService;

	@Override
	public void configure() throws Exception {
		JacksonDataFormat aeRequestJDF = jdfService.basic(AccountEnquiryRequest.class);
		JacksonDataFormat aeResponseJDF = jdfService.basic(AccountEnquiryResponse.class);

		onException(Exception.class)
			.log(LoggingLevel.ERROR, "[${header.cb_msgname}:${header.cb_e2eid}] Call CB Error.")
			.log(LoggingLevel.ERROR, "${exception.stacktrace}")
			.process(cbFaultProcessor)
			.marshal(aeResponseJDF)
			.continued(true);

		from("direct:cb_ae").routeId("komi.cb.ae")

			.setHeader("cb_msgname", simple("${exchangeProperty[prop_process_data.inbMsgName]}"))
			.setHeader("cb_e2eid", simple("${exchangeProperty[prop_process_data.endToEndId]}"))
			.setProperty("cb_request", simple("${body}"))
			
			.log(LoggingLevel.DEBUG,"komi.cb.ae", "[${header.cb_msgname}:${header.cb_e2eid}] Terima di corebank: ${body}")
					
			.setHeader("cb_requestName", constant("accountenquiry"))
			.setHeader("cb_url", simple("{{komi.url.isoadapter.accountinquiry}}"))
			.marshal(aeRequestJDF)
			
			.setProperty("cb_request_str", simple("${body}"))
			
	 		.log("[${header.cb_msgname}:${header.cb_e2eid}] POST ${header.cb_url}")
	 		.log("[${header.cb_msgname}:${header.cb_e2eid}] CB Request: ${body}")
			
			.setHeader("HttpMethod", constant("POST"))
			.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
			.enrich().simple("${header.cb_url}?bridgeEndpoint=true").aggregationStrategy(enrichmentAggregator)
			
			.convertBodyTo(String.class)
			.log("[${header.cb_msgname}:${header.cb_e2eid}] CB response: ${body}")

			.unmarshal(aeResponseJDF)
			.setHeader("cb_response", simple("${body.status}"))
			.setHeader("cb_reason", simple("${body.reason}"))
			
			.process(new Processor() {
				public void process(Exchange exchange) throws Exception {
					ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
					processData.setCorebankResponse(exchange.getMessage().getBody());
				}
			})
		;
		
	}

}
