package bifast.inbound.route;

import java.time.Instant;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.processor.CheckRequestMsgProc;
import bifast.inbound.service.JacksonDataFormatService;
import bifast.inbound.service.RefUtils;
import bifast.library.iso20022.custom.BusinessMessage;


@Component
public class InboundRoute extends RouteBuilder {

	@Autowired private CheckRequestMsgProc checkRequestMsgProcessor;
	@Autowired private JacksonDataFormatService jdfService;

	@Override
	public void configure() throws Exception {

		JacksonDataFormat jsonBusinessMessageDataFormat = jdfService.wrapUnwrapRoot(BusinessMessage.class);

		from("direct:receive").routeId("komi.inboundRoute")
			.convertBodyTo(String.class)
	
			.log(LoggingLevel.DEBUG,"komi.jsonEndpoint", "-------****------")
			.log("Terima: ${body}")
		
			.process(new Processor() {
				public void process(Exchange exchange) throws Exception {
					ProcessDataPojo processData = new ProcessDataPojo();
					processData.setTextDataReceived(exchange.getMessage().getBody(String.class));
					processData.setStartTime(Instant.now());
					processData.setKomiTrnsId(RefUtils.genKomiTrnsId());
					exchange.setProperty("prop_process_data", processData);
				}
			})

			.unmarshal(jsonBusinessMessageDataFormat)  // ubah ke pojo BusinessMessage

			.process(checkRequestMsgProcessor) 
		
			.choice().id("komi.dispatcher")

				.when().simple("${exchangeProperty.msgName} == 'Settl'")   // terima settlement
					.to("direct:settlement")
					.setBody(constant(""))

				.when().simple("${exchangeProperty.msgName} == 'PrxNtf'")  
					.to("direct:proxynotif")
					.setBody(constant(""))

				.when().simple("${exchangeProperty.msgName} == 'AccEnq'")   // terima account enquiry
					.to("direct:accountenq")

				.when().simple("${exchangeProperty.msgName} == 'CrdTrn'")    // terima credit transfer
					.to("direct:crdttransfer")
					.setHeader("hdr_toBIobj", simple("${body}"))

				.when().simple("${exchangeProperty.msgName} == 'RevCT'")     // reverse CT
					.to("direct:reverct")
					.setHeader("hdr_toBIobj", simple("${body}"))

				.when().simple("${exchangeProperty.msgName} == 'MsgRjct'")     // reverse CT
//					.to("direct:reverct")
					.setBody(constant(""))

				.otherwise()	
					.log("[Inbound] Message ${exchangeProperty.msgName} tidak dikenal")
			.end()
	
			// kirim log notif ke Portal
			.wireTap("direct:portalnotif")
				
			.filter().simple("${exchangeProperty.prop_process_data.inbMsgName} !in 'Settl, PrxNtf'")
				.marshal(jsonBusinessMessageDataFormat).convertBodyTo(String.class)
				.log("[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] Response: ${body}")
			.end()
				
			.log("[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] completed.")
			.removeHeaders("*")

		;


	}
}
