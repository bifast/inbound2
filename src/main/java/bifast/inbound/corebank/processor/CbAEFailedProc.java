package bifast.inbound.corebank.processor;

import java.lang.reflect.Method;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import bifast.inbound.corebank.isopojo.AccountEnquiryResponse;

@Component
public class CbAEFailedProc implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		Object objException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Object.class);

		int statusCode = 500;
		try {
			Method getStatusCode = objException.getClass().getMethod("getStatusCode");
			statusCode = (int) getStatusCode.invoke(objException);
		} catch(NoSuchMethodException noMethodE) {}

//		FaultPojo fault = new FaultPojo();		
		AccountEnquiryResponse resp = new AccountEnquiryResponse();

		String description = "Check error log";
		try {
			Method getMessage = objException.getClass().getMethod("getMessage");
			description = (String) getMessage.invoke(objException);
			description = objException.getClass().getSimpleName() + ": " + description;
			if (description.length()>250)
				description = description.substring(0,249);
		}
		catch(NoSuchMethodException noMethodE) {
			description = "Check error log";
		}

		if (statusCode == 504) {
			resp.setStatus("RJCT");
			resp.setReason("U900");
		}
		else {
			resp.setStatus("RJCT");
			resp.setReason("U901");
		}

		exchange.getMessage().setBody(resp, AccountEnquiryResponse.class);
	}
}
