package bifast.inbound.reversecrdttrns.processor;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.model.CreditTransfer;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs008Pojo;
import bifast.inbound.repository.CreditTransferRepository;
import bifast.inbound.service.CallRouteService;
import bifast.library.iso20022.custom.BusinessMessage;

@Component
public class SaveRevCTProc implements Processor {

	@Autowired private CreditTransferRepository creditTrnRepo;
	@Autowired private CallRouteService callRouteService;

	@Override
	public void process(Exchange exchange) throws Exception {
		 
		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
		FlatPacs008Pojo flatReq = (FlatPacs008Pojo)processData.getBiRequestFlat();
		
		CreditTransfer ct = new CreditTransfer();

		ct.setKomiTrnsId(processData.getKomiTrnsId());
		
		String fullReqMsg = callRouteService.encryptBusinessMessage(processData.getBiRequestMsg());
		String fullRespMsg = callRouteService.encryptBusinessMessage(exchange.getMessage().getBody(BusinessMessage.class));
		
		ct.setFullRequestMessage(fullReqMsg);
		ct.setFullResponseMsg(fullRespMsg);
		
		if (null != processData.getBiResponseMsg()) {
			BusinessMessage respBi = processData.getBiResponseMsg();
			String responseCode = respBi.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getTxSts();
			if (respBi.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getStsRsnInf().get(0).getAddtlInf().size()>0) {
				String errMsg = respBi.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getStsRsnInf().get(0).getAddtlInf().get(0);
				ct.setErrorMessage(errMsg);
			}

			ct.setResponseCode(responseCode);
			ct.setCrdtTrnResponseBizMsgIdr(respBi.getAppHdr().getBizMsgIdr());
			ct.setReasonCode(respBi.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0).getStsRsnInf().get(0).getRsn().getPrtry());
			ct.setCallStatus("SUCCESS");
			if (responseCode.equals("ACTC")) {
				ct.setCbStatus("PENDING");
				ct.setSettlementConfBizMsgIdr("WAITING");
			}
		}
		
		ct.setCihubRequestDT(processData.getReceivedDt());
		long timeElapsed = Duration.between(processData.getStartTime(), Instant.now()).toMillis();
		ct.setCihubElapsedTime(timeElapsed);

		ct.setCreateDt(LocalDateTime.now());
		ct.setLastUpdateDt(LocalDateTime.now());
	
		ct.setAmount(flatReq.getAmount());
		ct.setCrdtTrnRequestBizMsgIdr(flatReq.getBizMsgIdr());
		ct.setEndToEndId(flatReq.getEndToEndId());
		ct.setCreditorAccountNumber(flatReq.getCreditorAccountNo());

		if (!(null == flatReq.getCreditorAccountType()))
			ct.setCreditorAccountType(flatReq.getCreditorAccountType());
		
		if (null!=flatReq.getCreditorType()) 
			ct.setCreditorType(flatReq.getCreditorType());

		if (!(null==flatReq.getCreditorId()))
			ct.setCreditorId(flatReq.getCreditorId());
				
		
		ct.setDebtorAccountNumber(flatReq.getDebtorAccountNo());
		ct.setDebtorAccountType(flatReq.getDebtorAccountType());
		
		if (null != flatReq.getDebtorType())
				ct.setDebtorType(flatReq.getDebtorType());

		if (!(null==flatReq.getDebtorId()))
			ct.setDebtorId(flatReq.getDebtorId());

		ct.setMsgType("Credit Transfer Reversal");
				
		ct.setOriginatingBank(flatReq.getDebtorAgentId());
		ct.setRecipientBank(flatReq.getCreditorAgentId());
		
		if (null != flatReq.getCpyDplct())
			ct.setCpyDplct(flatReq.getCpyDplct());
		
		creditTrnRepo.save(ct);
	}
	
}