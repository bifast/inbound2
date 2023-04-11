package bifast.inbound.paymentstatus;

import java.sql.Timestamp;
import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class ProcessQuerySAFProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> arr = exchange.getMessage().getBody(HashMap.class);
		
		CTQryDTO ct = new CTQryDTO();
		ct.setId((Long)arr.get("id"));
		ct.setKomiTrnsId((String)arr.get("komi_id"));
		ct.setEndToEndId((String) arr.get("e2e_id") );
		ct.setCtFullText(String.valueOf(arr.get("txtctreq")));
		ct.setRecipientBank((String)arr.get("recpt_bank"));
		ct.setReqBizmsgid((String)arr.get("req_bizmsgid"));
		
		Timestamp ts = (Timestamp) arr.get("last_update_dt");
		ct.setLastUpdateDt(ts.toLocalDateTime());
		
		ct.setPsCounter((int) arr.get("ps_counter"));
		ct.setPsCounter(ct.getPsCounter()+1);
			
		ObjectMapper map = new ObjectMapper();
		map.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		
		
		exchange.setProperty("pr_psrequest", ct);
	
		
		exchange.setProperty("end2endid", ct.getEndToEndId());

		exchange.getMessage().setBody(ct.getCtFullText());

	}

}
