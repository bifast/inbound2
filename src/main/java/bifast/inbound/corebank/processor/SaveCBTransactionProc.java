package bifast.inbound.corebank.processor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.corebank.isopojo.CreditRequest;
import bifast.inbound.corebank.isopojo.DebitReversalRequest;
import bifast.inbound.corebank.isopojo.SettlementRequest;
import bifast.inbound.model.CorebankTransaction;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.repository.CorebankTransactionRepository;

@Component
public class SaveCBTransactionProc implements Processor {
	@Autowired private CorebankTransactionRepository cbRepo;
	
	private static Logger logger = LoggerFactory.getLogger(SaveCBTransactionProc.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String msgName = exchange.getMessage().getHeader("cb_msgname", String.class);
		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
		
		String komiTrnsId = exchange.getProperty("pr_komitrnsid", String.class);
		
		String strRequest = exchange.getProperty("cb_request_str", String.class);
		Object oCbRequest = exchange.getProperty("cb_request", Object.class);
		
		String response = exchange.getMessage().getHeader("cb_response", String.class);
		String reason = exchange.getMessage().getHeader("cb_reason", String.class);
		
		CorebankTransaction corebankTrans = new CorebankTransaction();

		corebankTrans.setUpdateTime(LocalDateTime.now());
		corebankTrans.setFullTextRequest(strRequest);
		corebankTrans.setTrnsDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		corebankTrans.setKomiTrnsId(komiTrnsId);

		corebankTrans.setReason(reason);
		corebankTrans.setResponse(response);

		logger.debug("[" + msgName + ":" + processData.getEndToEndId() + "] Akan save " + oCbRequest.getClass().getSimpleName());
		
		if (oCbRequest.getClass().getSimpleName().equals("CreditRequest")) {
			CreditRequest req = (CreditRequest) oCbRequest;
			
			corebankTrans.setCreditAmount(new BigDecimal(req.getAmount()));
			corebankTrans.setCstmAccountName(req.getCreditorName());
			corebankTrans.setCstmAccountNo(req.getCreditorAccountNumber());
			corebankTrans.setCstmAccountType(req.getCreditorAccountType());
			corebankTrans.setDateTime(req.getDateTime());
//			corebankTrans.setDebitAmount(null);
			corebankTrans.setFeeAmount(new BigDecimal(req.getFeeTransfer()));
			
			corebankTrans.setKomiNoref(req.getNoRef());
			corebankTrans.setOrgnlChnlNoref(req.getOriginalNoRef());
			corebankTrans.setOrgnlDateTime(req.getOriginalDateTime());
						
			corebankTrans.setTransactionType("Credit");
		}
		
		else if (oCbRequest.getClass().getSimpleName().equals("SettlementRequest")) {
			SettlementRequest req = (SettlementRequest) oCbRequest;
			
			corebankTrans.setDateTime(req.getDateTime());
			corebankTrans.setKomiNoref(req.getNoRef());
			corebankTrans.setOrgnlChnlNoref(req.getOriginalNoRef());
						
			corebankTrans.setTransactionType("Settlement");
		}
		
		else if (oCbRequest.getClass().getSimpleName().equals("DebitReversalRequest")) {
			DebitReversalRequest req = (DebitReversalRequest) oCbRequest;
			
			corebankTrans.setDateTime(req.getDateTime());
			corebankTrans.setKomiNoref(req.getNoRef());
			corebankTrans.setOrgnlChnlNoref(req.getOriginalNoRef());
						
			corebankTrans.setTransactionType("DebitReversal");
		}

		
		cbRepo.save(corebankTrans);
	}

}
