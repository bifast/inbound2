package bifast.inbound.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class InboundJsonRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration().component("servlet");
			
		rest("/json").post("/service").consumes("application/json").to("direct:receive");

	}
}
