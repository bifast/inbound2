package bifast.inbound.reversecrdttrns.processor;

import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.model.CreditTransfer;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs008Pojo;
import bifast.inbound.repository.CreditTransferRepository;

@Component
public class CheckDebitHistoryProc implements Processor {
	@Autowired private CreditTransferRepository ctRepo;
	
	private static Logger logger = LoggerFactory.getLogger(CheckDebitHistoryProc.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
		FlatPacs008Pojo request = exchange.getProperty("flatRequest", FlatPacs008Pojo.class);
		
		Optional<CreditTransfer> oCrdtTrns = ctRepo.getSuccessByEndToEndId(request.getOrgnlEndToEndId());
	
		if (oCrdtTrns.isPresent()) {
			CreditTransfer ct = oCrdtTrns.get();

			if (ct.getAmount().compareTo(request.getAmount()) != 0) {
				logger.debug("["+ processData.getInbMsgName() + ":" + processData.getEndToEndId() + "] "
						+ "Lookup outbound transaction: " + request.getOrgnlEndToEndId() + " amount is not match!");
				exchange.setProperty("pr_revCTCheckRsl", "DataNotMatch");
			}
			else if (!(ct.getDebtorAccountNumber().equals(request.getCreditorAccountNo()))) {
				logger.debug("["+ processData.getInbMsgName() + ":" + processData.getEndToEndId() + "] "
						+ " creditor account is not match!");
				exchange.setProperty("pr_revCTCheckRsl", "DataNotMatch");
			}
			else if (!(ct.getCreditorAccountNumber().equals(request.getDebtorAccountNo()))) {
				logger.debug("["+ processData.getInbMsgName() + ":" + processData.getEndToEndId() + "] "
						+ " debtor account is not match!");
				exchange.setProperty("pr_revCTCheckRsl", "DataNotMatch");
			}

			else {
				logger.debug("["+ processData.getInbMsgName() + ":" + processData.getEndToEndId() + "] "
						+ "Lookup outbound transaction: " + request.getOrgnlEndToEndId() + " found.");
				exchange.setProperty("pr_revCTCheckRsl", "DataMatch");
			}
		}
		
		else {
			logger.debug("["+ processData.getInbMsgName() + ":" + processData.getEndToEndId() + "] "
					+ "Lookup outbound transaction: " + request.getOrgnlEndToEndId() + " not found!");
			exchange.setProperty("pr_revCTCheckRsl", "NotFound");
		}
	}

}
