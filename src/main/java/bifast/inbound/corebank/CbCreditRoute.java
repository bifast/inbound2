package bifast.inbound.corebank;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import bifast.inbound.corebank.isopojo.CreditRequest;
import bifast.inbound.corebank.isopojo.CreditResponse;
import bifast.inbound.corebank.processor.CbCreditFailedProc;
import bifast.inbound.corebank.processor.SaveCBTransactionProc;
import bifast.inbound.processor.EnrichmentAggregator;
import bifast.inbound.service.JacksonDataFormatService;

@Component
public class CbCreditRoute extends RouteBuilder {
	@Autowired private JacksonDataFormatService jdfService;
	@Autowired private EnrichmentAggregator enrichmentAggregator;
	@Autowired private CbCreditFailedProc cbFaultProcessor;
	@Autowired private SaveCBTransactionProc saveCBTransactionProc;

	@Override
	public void configure() throws Exception {
		JacksonDataFormat creditRequestJDF = jdfService.basic(CreditRequest.class);
		JacksonDataFormat creditResponseJDF = jdfService.basic(CreditResponse.class);

		onException(HttpOperationFailedException.class).onWhen(simple("${exception.statusCode} == '504'"))
			.log(LoggingLevel.ERROR, "[${header.cb_msgname}:${header.cb_e2eid}] Corebank TIMEOUT: \n ${exception.message}")
			.process(cbFaultProcessor).marshal(creditResponseJDF)
			.continued(true);

		onException(Exception.class)
			.log(LoggingLevel.ERROR, "[${header.cb_msgname}:${header.cb_e2eid}] Call CB Error.")
			.log(LoggingLevel.ERROR, "${exception.stacktrace}")
			.process(cbFaultProcessor).marshal(creditResponseJDF)
			.continued(true);

		from("direct:isoadpt-credit").routeId("komi.cb.credit")

			.setHeader("cb_msgname", simple("${exchangeProperty[prop_process_data.inbMsgName]}"))
			.setHeader("cb_e2eid", simple("${exchangeProperty[prop_process_data.endToEndId]}"))
			.setProperty("cb_request", simple("${body}"))
			
			.log(LoggingLevel.DEBUG,"komi.isoadapter", "[${header.cb_msgname}:${header.cb_e2eid}] Terima di corebank: ${body}")
			
			.setHeader("cb_requestName", constant("credit"))
			.setHeader("cb_url", simple("{{komi.url.isoadapter.credit}}"))
			.marshal(creditRequestJDF)

			.setProperty("cb_request_str", simple("${body}"))
			
	 		.log("[${header.cb_msgname}:${header.cb_e2eid}] POST ${header.cb_url}")
	 		.log("[${header.cb_msgname}:${header.cb_e2eid}] CB Request: ${body}")

			.setHeader("HttpMethod", constant("POST"))
			.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
			.enrich().simple("${header.cb_url}?bridgeEndpoint=true").aggregationStrategy(enrichmentAggregator)
			.convertBodyTo(String.class)
			.log("[${header.cb_msgname}:${header.cb_e2eid}] CB response: ${body}")
			
			.unmarshal(creditResponseJDF)
			.setHeader("cb_response", simple("${body.status}"))
			.setHeader("cb_reason", simple("${body.reason}"))
			
			.process(saveCBTransactionProc)

		;
	}

}
