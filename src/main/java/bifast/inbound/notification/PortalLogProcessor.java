package bifast.inbound.notification;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.model.StatusReason;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs008Pojo;
import bifast.inbound.repository.StatusReasonRepository;
import bifast.library.iso20022.custom.BusinessMessage;

@Component
public class PortalLogProcessor implements Processor{
	@Autowired StatusReasonRepository statusReasonRepo;
	
//	private static Logger logger = LoggerFactory.getLogger(AEPortalLogProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
		String msgName = processData.getInbMsgName();
		BusinessMessage resp = processData.getBiResponseMsg();
		
		FlatPacs008Pojo req = (FlatPacs008Pojo) processData.getBiRequestFlat();
		
		PortalApiPojo logMsg = new PortalApiPojo();
		LogDataPojo data = new LogDataPojo();

		data.setStatus_code("SUCCESS");
		
		data.setResponse_code(resp.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getTxSts());
		
		String reasonCode = resp.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getStsRsnInf().get(0).getRsn().getPrtry();
		StatusReason stsRsn = statusReasonRepo.findById(reasonCode).orElse(new StatusReason());

		data.setReason_code(reasonCode);
		data.setReason_message(stsRsn.getDescription());

		if (null != resp.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getOrgnlTxRef().getCdtr())
			data.setRecipient_account_name(resp.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getOrgnlTxRef().getCdtr().getPty().getNm());

		if (null != resp.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getOrgnlTxRef().getDbtr())
			data.setSender_account_name(resp.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getOrgnlTxRef().getDbtr().getPty().getNm());
		
		data.setKomi_trx_no(processData.getKomiTrnsId());
		data.setKomi_unique_id(req.getEndToEndId());
		data.setChannel_type("99");
	
		data.setSender_bank(req.getDebtorAgentId());
		data.setTrx_type("I");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		data.setTrx_initiation_date(processData.getReceivedDt().format(formatter));

		long timeElapsed = Duration.between(processData.getReceivedDt(), LocalDateTime.now()).toMillis();
		data.setTrx_duration(String.valueOf(timeElapsed));

		if (msgName.equals("AccEnq"))
			logMsg.setCodelog("AE");
		else if (msgName.equals("CrdTrn"))
			logMsg.setCodelog("CT");
		else if (msgName.equals("RevCT"))
			logMsg.setCodelog("CT");

		data.setBifast_trx_no(req.getBizMsgIdr());
			
		data.setProxyFlag("T");
		data.setRecipient_bank(req.getToBic());
		data.setRecipient_account_no(req.getCreditorAccountNo());
		data.setSender_account_no(req.getDebtorAccountNo());

		DecimalFormat df = new DecimalFormat("#############.00");
		data.setTrx_amount(df.format(req.getAmount()));
		
		logMsg.setData(data);
		exchange.getMessage().setBody(logMsg);
		
	}

}
