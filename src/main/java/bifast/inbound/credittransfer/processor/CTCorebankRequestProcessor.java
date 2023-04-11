package bifast.inbound.credittransfer.processor;

import java.text.DecimalFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import bifast.inbound.config.Config;
import bifast.inbound.corebank.isopojo.CreditRequest;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs008Pojo;
import bifast.inbound.service.RefUtils;

@Component
public class CTCorebankRequestProcessor implements Processor {
	@Autowired Config config;

	@Value("${komi.isoadapter.merchant}")
	String merchant;

	@Value("${komi.isoadapter.terminal}")
	String terminal;

	@Value("${komi.isoadapter.txid}")
	String txid;

	@Override
	public void process(Exchange exchange) throws Exception {
		ProcessDataPojo processData= exchange.getProperty("prop_process_data", ProcessDataPojo.class);
//		String komiTrnsId = processData.getKomiTrnsId();
		
		FlatPacs008Pojo biReq = (FlatPacs008Pojo) processData.getBiRequestFlat();

		CreditRequest cbRequest = new CreditRequest();

		RefUtils.Ref ref = RefUtils.newRef();

		exchange.setProperty("cb_noref", ref.getNoRef());
		
		cbRequest.setRecipientBank(config.getBankcode());
		
		cbRequest.setDateTime(ref.getDateTime());
		cbRequest.setNoRef(ref.getNoRef());
		cbRequest.setOriginalNoRef(ref.getNoRef());
		cbRequest.setOriginalDateTime(ref.getDateTime());
		
		cbRequest.setMerchantType(merchant);
		cbRequest.setTerminalId(terminal);
		cbRequest.setTransactionId(txid);
		
		DecimalFormat df = new DecimalFormat("#############.00");
		cbRequest.setAmount(df.format(biReq.getAmount()));

		cbRequest.setCategoryPurpose(biReq.getCategoryPurpose());
		cbRequest.setCreditorAccountNumber(biReq.getCreditorAccountNo());
		cbRequest.setCreditorAccountType(biReq.getCreditorAccountType());

		if (null != biReq.getCreditorId())
			cbRequest.setCreditorId(biReq.getCreditorId());

		cbRequest.setCreditorName(biReq.getCreditorName());
		
		if (null != biReq.getCreditorAccountProxyId()) {
			cbRequest.setCreditorProxyId(biReq.getCreditorAccountProxyId());
			cbRequest.setCreditorProxyType(biReq.getCreditorAccountProxyType());
		}
		
		cbRequest.setCreditorResidentStatus(biReq.getCreditorResidentialStatus());
		cbRequest.setCreditorTownName(biReq.getCreditorTownName());
		cbRequest.setCreditorType(biReq.getCreditorType());
		
		cbRequest.setDebtorAccountNumber(biReq.getDebtorAccountNo());
		cbRequest.setDebtorAccountType(biReq.getDebtorAccountType());
		if (null != biReq.getDebtorId())
			cbRequest.setDebtorId(biReq.getDebtorId());
			
		cbRequest.setDebtorName(biReq.getDebtorName());
		cbRequest.setDebtorResidentStatus(biReq.getDebtorResidentialStatus());
		cbRequest.setDebtorTownName(biReq.getDebtorTownName());
		cbRequest.setDebtorType(biReq.getDebtorType());
		
		cbRequest.setFeeTransfer("0.00");

		if (!(null == biReq.getPaymentInfo()))
			cbRequest.setPaymentInformation(biReq.getPaymentInfo());
				
		exchange.getMessage().setBody(cbRequest);
	}

}
