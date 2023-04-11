package bifast.inbound.corebank.processor;

import java.lang.reflect.Method;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import bifast.inbound.pojo.FaultPojo;


@Component
public class CbCallFaultProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		Object objException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Object.class);
		String exceptionClassName = objException.getClass().getName();		
			
		int statusCode = 500;
		
		try {
			Method getStatusCode = objException.getClass().getMethod("getStatusCode");
			statusCode = (int) getStatusCode.invoke(objException);
		} catch(NoSuchMethodException noMethodE) {}

		FaultPojo fault = new FaultPojo();		

		if (exceptionClassName.equals("java.net.SocketTimeoutException"))
			fault.setCallStatus("TIMEOUT");
		else if (statusCode == 504) {
			fault.setCallStatus("TIMEOUT");
		}
		else 
			fault.setCallStatus("ERROR");
		
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
		fault.setErrorMessage(description);

		if (statusCode == 504) {
			fault.setResponseCode("RJCT");
			fault.setReasonCode("U900");
		}
		else {
			fault.setResponseCode("RJCT");
			fault.setReasonCode("U901");
		}

		exchange.getMessage().setBody(fault, FaultPojo.class);
		exchange.getMessage().setHeader("cb_response", fault.getResponseCode());
		exchange.getMessage().setHeader("cb_reason", fault.getReasonCode());
		
		
	}

}
