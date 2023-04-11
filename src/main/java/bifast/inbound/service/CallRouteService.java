package bifast.inbound.service;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import bifast.library.iso20022.custom.BusinessMessage;

@Service
public class CallRouteService {
	@Autowired private UtilService utilService;
	@Autowired private CamelContext camelContext;
	
	public String encryptBusinessMessage (BusinessMessage bm) throws JsonProcessingException {
		FluentProducerTemplate template = camelContext.createFluentProducerTemplate();
		String str = utilService.serializeBusinessMassage(bm);
		String encrBody = template.withBody(str).to("direct:encrypbody").request(String.class);
		return encrBody;
	}
	
	public BusinessMessage decryptBusinessMessage (String str) throws JsonProcessingException {
		FluentProducerTemplate template = camelContext.createFluentProducerTemplate();
		BusinessMessage decrBody = template.withBody(str).to("direct:decryp_unmarshal").request(BusinessMessage.class);
		return decrBody;
	}
	
	public Object callRoute (Exchange exchange, String to) {
		CamelContext context = exchange.getContext();
		context.createProducerTemplate().send(to, exchange);
		Object result = exchange.getMessage().getBody(Object.class);
		return result;
	}

}
