package bifast.inbound.paymentstatus;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class PSFilter {

	public boolean timeIsDue (Exchange exchange) {
		CTQryDTO ct = exchange.getProperty("pr_psrequest", CTQryDTO.class);
		LocalDateTime orgnlTime = ct.getLastUpdateDt();
		Duration duration = Duration.between(orgnlTime, LocalDateTime.now());
		
		if (ct.getPsCounter()==1) 
			return duration.getSeconds() > 90;
		
		else if (ct.getPsCounter()==2) 
			return duration.getSeconds() > 150;
		
		else if (ct.getPsCounter()==3) 
			return duration.getSeconds() > 270;
		
		else 
			return duration.getSeconds() > 510;
		
	}
	
//	public boolean sttlIsNotFound (Exchange exchange) {
//		UndefinedCTPojo ct = exchange.getMessage().getBody(UndefinedCTPojo.class);
//		return ct.getPsStatus() == "STTL_NOTFOUND";
//	}
//
//	public boolean sttlIsFound (Exchange exchange) {
//		UndefinedCTPojo ct = exchange.getMessage().getBody(UndefinedCTPojo.class);
//		return ct.getPsStatus() == "STTL_FOUND";
//	}

}
