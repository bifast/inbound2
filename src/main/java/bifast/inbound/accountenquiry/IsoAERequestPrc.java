package bifast.inbound.accountenquiry;

import java.text.DecimalFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import bifast.inbound.corebank.isopojo.AccountEnquiryRequest;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs008Pojo;
import bifast.inbound.service.TransRef;

@Component
public class IsoAERequestPrc implements Processor {
	
	@Value("${komi.isoadapter.merchant}")
	String merchant;
	@Value("${komi.isoadapter.terminal}")
	String terminal;
	@Value("${komi.bankcode}")
	String corebic;
	@Value("${komi.isoadapter.txid}")
	String txid;

	@Override
	public void process(Exchange exchange) throws Exception {
		
		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
		FlatPacs008Pojo aeRequest = (FlatPacs008Pojo) processData.getBiRequestFlat();
		
		AccountEnquiryRequest req = new AccountEnquiryRequest();

		req.setAccountNumber(aeRequest.getCreditorAccountNo());
		
		DecimalFormat df = new DecimalFormat("#############.00");
		req.setAmount(df.format(aeRequest.getAmount()));
		
		req.setCategoryPurpose(aeRequest.getCategoryPurpose());
		TransRef.Ref ref = TransRef.newRef();
		req.setDateTime(ref.getDateTime());
		req.setMerchantType(merchant);
		req.setNoRef(ref.getNoRef());
		req.setRecipientBank(corebic);
		req.setTerminalId(terminal);
		req.setTransactionId(txid);

		exchange.getMessage().setBody(req);
		
	}

}
