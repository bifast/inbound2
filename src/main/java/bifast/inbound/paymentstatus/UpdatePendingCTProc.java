package bifast.inbound.paymentstatus;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.model.CreditTransfer;
import bifast.inbound.repository.CreditTransferRepository;

@Component
public class UpdatePendingCTProc implements Processor {
	@Autowired private CreditTransferRepository ctRepo;

	@Override
	public void process(Exchange exchange) throws Exception {
		String response = exchange.getProperty("pr_psresponse", String.class);
		String reason = exchange.getProperty("pr_psreason", String.class);
		CTQryDTO qry = exchange.getProperty("pr_psrequest", CTQryDTO.class);
		
		Optional<CreditTransfer> oCt = ctRepo.findById(qry.getId());
		if (oCt.isPresent()) {
			CreditTransfer ct = oCt.get();
			ct.setPsCounter(ct.getPsCounter()+1);
			ct.setLastUpdateDt(LocalDateTime.now());
			
			if (response.equals("ACSC")) 
				ct.setSettlementConfBizMsgIdr("RECEIVED");
			else
				ct.setSettlementConfBizMsgIdr(reason);

			ctRepo.save(ct);		
		}
	}
}
