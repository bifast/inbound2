package bifast.inbound.notification;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import bifast.inbound.service.JacksonDataFormatService;

@Component
public class NotificationRoute extends RouteBuilder{
	@Autowired private PortalLogProcessor portalLogProcessor;
	@Autowired private EventNotificationProcessor eventNotifProcessor;
	@Autowired private ProxyNotifProcessor proxyNotifProc;
	@Autowired private JacksonDataFormatService jdfService;

	@Override
	public void configure() throws Exception {

		JacksonDataFormat portalJdf = jdfService.wrapRoot(PortalApiPojo.class);

		onException(Exception.class)
			.maximumRedeliveries(2).redeliveryDelay(5000)
    		.log(LoggingLevel.ERROR, "komi.portalnotif", "Error Log-notif ${body}")
    		.log(LoggingLevel.ERROR, "${exception.stacktrace}")
    		.handled(true);

		from("direct:proxynotif").routeId("komi.prxnotif")
			.log("Proxy Port Notification")
			.process(proxyNotifProc)
		;

		from("direct:eventnotif").routeId("komi.eventnotif")
			.log("Ada notifikasi")
			.process(eventNotifProcessor)
			
			//TODO info notification
		;
		
		from("direct:portalnotif").routeId("komi.portalnotif")
			.filter().simple("${exchangeProperty.msgName} in 'AccEnq,CrdTrn,RevCT' ")

			.process(portalLogProcessor)
			.marshal(portalJdf)
			.removeHeaders("hdr_*") 
			.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
			.to("rest:post:?host={{komi.url.portalapi}}&bridgeEndpoint=true")

		;

		
	}

}
