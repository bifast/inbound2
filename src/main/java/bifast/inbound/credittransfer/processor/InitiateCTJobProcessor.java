package bifast.inbound.credittransfer.processor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs008Pojo;
import bifast.inbound.service.CallRouteService;
import bifast.inbound.service.FlattenIsoMessageService;
import bifast.library.iso20022.custom.BusinessMessage;

@Component
public class InitiateCTJobProcessor implements Processor{
	@Autowired private CallRouteService routeService;
	@Autowired private FlattenIsoMessageService flatMsgService;

	@Override
	public void process(Exchange exchange) throws Exception {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> arr = exchange.getProperty("ctsaf_qryresult",HashMap.class);

		BusinessMessage orgnlCTRequest = routeService.decryptBusinessMessage(String.valueOf(arr.get("ct_msg"))); 
		
		ProcessDataPojo processData = new ProcessDataPojo();
		FlatPacs008Pojo flat008 = flatMsgService.flatteningPacs008(orgnlCTRequest); 
		
		processData.setBiRequestFlat(flat008);
	
		processData.setBiRequestMsg(orgnlCTRequest);
		processData.setStartTime(Instant.now());
		processData.setInbMsgName("CTSAF");
		processData.setEndToEndId(flat008.getEndToEndId());
		processData.setKomiTrnsId(String.valueOf(arr.get("komi_trns_id")));
		processData.setReceivedDt(LocalDateTime.now());

		exchange.setProperty("prop_process_data", processData);
		
		exchange.setProperty("pr_komitrnsid", String.valueOf(arr.get("komi_trns_id")));

		
	}

}
