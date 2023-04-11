package bifast.inbound.notification;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.model.Proxy;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPrxy901Pojo;
import bifast.inbound.repository.ProxyRepository;

@Component
public class ProxyNotifProcessor implements Processor {
	@Autowired private ProxyRepository prxRepo;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);

		FlatPrxy901Pojo prxReq = (FlatPrxy901Pojo) processData.getBiRequestFlat();

		Optional<Proxy> oProxy = prxRepo.getByProxyTypeAndProxyValue(prxReq.getOrgnlProxyType(), prxReq.getOrgnlProxyValue());
		
		if (oProxy.isPresent()) {
			
			Proxy proxy = oProxy.get();
			
			proxy.setRegisterBank(prxReq.getNewBankId());
			proxy.setProxyStatus("PORTED");
			proxy.setUpdateDt(LocalDateTime.now());
			prxRepo.save(proxy);
			
		}
		
	}

}
