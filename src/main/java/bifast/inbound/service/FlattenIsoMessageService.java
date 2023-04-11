package bifast.inbound.service;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Service;

import bifast.inbound.pojo.flat.FlatAdmi002Pojo;
import bifast.inbound.pojo.flat.FlatAdmi004Pojo;
import bifast.inbound.pojo.flat.FlatPacs002Pojo;
import bifast.inbound.pojo.flat.FlatPacs008Pojo;
import bifast.inbound.pojo.flat.FlatPrxy901Pojo;
import bifast.library.iso20022.custom.BusinessMessage;
import bifast.library.iso20022.head001.BusinessApplicationHeaderV01;
import bifast.library.iso20022.pacs002.GroupHeader91;
import bifast.library.iso20022.pacs002.OriginalGroupHeader17;
import bifast.library.iso20022.pacs002.PaymentTransaction110;
import bifast.library.iso20022.pacs008.FIToFICustomerCreditTransferV08;
import bifast.library.iso20022.prxy901.ProxyAccount1;

@Service
public class FlattenIsoMessageService {

//	private static Logger logger = LoggerFactory.getLogger(FlattenIsoMessageService.class);

	public FlatPacs002Pojo flatteningPacs002 (BusinessMessage busMsg) {

		FlatPacs002Pojo flatMsg = new FlatPacs002Pojo();

		BusinessApplicationHeaderV01 hdr = busMsg.getAppHdr();
		
		flatMsg.setFrBic(hdr.getFr().getFIId().getFinInstnId().getOthr().getId());
		flatMsg.setToBic(hdr.getTo().getFIId().getFinInstnId().getOthr().getId());
		flatMsg.setBizMsgIdr(hdr.getBizMsgIdr());
		flatMsg.setMsgDefIdr(hdr.getMsgDefIdr());

		if (!(null == hdr.getBizSvc()))
			flatMsg.setBizSvc(hdr.getBizSvc());

		flatMsg.setCreDt(strTgl(hdr.getCreDt()));

		if (!(null == hdr.getCpyDplct()))
			flatMsg.setCpyDplct(hdr.getCpyDplct().value());

		if (!(null == hdr.isPssblDplct()))
			flatMsg.setPssblDplct(hdr.isPssblDplct());
		
		GroupHeader91 grpHdr = busMsg.getDocument().getFiToFIPmtStsRpt().getGrpHdr();

		flatMsg.setMsgId(grpHdr.getMsgId());
		
//		flatMsg.setCreDtTm(strTgl(grpHdr.getCreDtTm()));
		flatMsg.setCreDtTm(grpHdr.getCreDtTm());
		
		OriginalGroupHeader17 orgnlGrpInf = new OriginalGroupHeader17();
		if (busMsg.getDocument().getFiToFIPmtStsRpt().getOrgnlGrpInfAndSts().size()>0) {
			orgnlGrpInf = busMsg.getDocument().getFiToFIPmtStsRpt().getOrgnlGrpInfAndSts().get(0);
		
			if (!(null == orgnlGrpInf.getOrgnlMsgId()))
				flatMsg.setOrgnlMsgId(orgnlGrpInf.getOrgnlMsgId());
	
			if (!(null == orgnlGrpInf.getOrgnlMsgNmId()))
				flatMsg.setOrgnlMsgNmId(orgnlGrpInf.getOrgnlMsgNmId());
			
			if (!(null == orgnlGrpInf.getOrgnlCreDtTm()))
				flatMsg.setOrgnlCreDtTm(strTgl(orgnlGrpInf.getOrgnlCreDtTm()));
		}

		PaymentTransaction110 txInfAndSts = new PaymentTransaction110();

		txInfAndSts = busMsg.getDocument().getFiToFIPmtStsRpt().getTxInfAndSts().get(0);

		if (!(null == txInfAndSts.getOrgnlEndToEndId()))
			flatMsg.setOrgnlEndToEndId(txInfAndSts.getOrgnlEndToEndId());

		flatMsg.setTransactionStatus(txInfAndSts.getTxSts());
		
		if (txInfAndSts.getStsRsnInf().size()>0) {
			flatMsg.setReasonCode(txInfAndSts.getStsRsnInf().get(0).getRsn().getPrtry());

			if (txInfAndSts.getStsRsnInf().get(0).getAddtlInf().size()>0)
				flatMsg.setRsnInfAddtlInf(txInfAndSts.getStsRsnInf().get(0).getAddtlInf().get(0));
			
		}

		if (!(null == txInfAndSts.getOrgnlTxRef())) {
			
			if (!(null == txInfAndSts.getOrgnlTxRef().getIntrBkSttlmDt()))
				flatMsg.setIntrBkSttlmDt(strTgl(txInfAndSts.getOrgnlTxRef().getIntrBkSttlmDt()));
		
			if (!(null == txInfAndSts.getOrgnlTxRef().getDbtr())) {
			
				flatMsg.setDbtrNm(txInfAndSts.getOrgnlTxRef().getDbtr().getPty().getNm());

			}

			if (!(null == txInfAndSts.getOrgnlTxRef().getDbtrAcct())) {

				flatMsg.setDbtrAcctId(txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr().getId());
				
				if (!(null== txInfAndSts.getOrgnlTxRef().getDbtrAcct().getTp()))
						flatMsg.setDbtrAcctTp(txInfAndSts.getOrgnlTxRef().getDbtrAcct().getTp().getPrtry());
				
			}

			if (!(null == txInfAndSts.getOrgnlTxRef().getDbtrAgt())) {
				flatMsg.setDbtrAgtFinInstnId(txInfAndSts.getOrgnlTxRef().getDbtrAgt().getFinInstnId().getOthr().getId());
				
			}

			if (!(null == txInfAndSts.getOrgnlTxRef().getCdtrAgt())) {
				flatMsg.setCdtrAgtFinInstnId(txInfAndSts.getOrgnlTxRef().getCdtrAgt().getFinInstnId().getOthr().getId());
			}
			
			if (!(null == txInfAndSts.getOrgnlTxRef().getCdtr())) {
				
				flatMsg.setCdtrNm(txInfAndSts.getOrgnlTxRef().getCdtr().getPty().getNm());

			}

			if (!(null == txInfAndSts.getOrgnlTxRef().getCdtrAcct())) {
				
				flatMsg.setCdtrAcctId(txInfAndSts.getOrgnlTxRef().getCdtrAcct().getId().getOthr().getId());
				
				if (null != txInfAndSts.getOrgnlTxRef().getCdtrAcct().getTp())
					flatMsg.setCdtrAcctTp(txInfAndSts.getOrgnlTxRef().getCdtrAcct().getTp().getPrtry());
				
			}
				
		}
		
		if (txInfAndSts.getSplmtryData().size() > 0) {
			
			if (!(null == txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getDbtr())) {
			
				flatMsg.setDbtrTp(txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getTp());
				flatMsg.setDbtrId(txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getId());
				flatMsg.setDbtrRsdntSts(txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getRsdntSts());
				flatMsg.setDbtrTwnNm(txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getTwnNm());

			}					

			if (!(null == txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getCdtr())) {
				
				flatMsg.setCdtrTp(txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getTp());
				flatMsg.setCdtrId(txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getId());
				flatMsg.setCdtrRsdntSts(txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getRsdntSts());
				flatMsg.setCdtrTwnNm(txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getTwnNm());
				
			}

//			if (!(null == txInfAndSts.getSplmtryData().get(0).getEnvlp().getDbtrAgtAcct())) {
//				flatMsg.setDbtrAgtAcctId(txInfAndSts.getSplmtryData().get(0).getEnvlp().getDbtrAgtAcct().getId().getOthr().getId());
//			}
//			
//			if (!(null == txInfAndSts.getSplmtryData().get(0).getEnvlp().getCdtrAgtAcct())) {
//				flatMsg.setCdtrAgtAcctId(txInfAndSts.getSplmtryData().get(0).getEnvlp().getCdtrAgtAcct().getId().getOthr().getId());
//			}
			
		}
		
		return flatMsg;
	}
	

	public FlatPacs008Pojo flatteningPacs008 (BusinessMessage busMsg) {
		
		FlatPacs008Pojo flatMsg = new FlatPacs008Pojo();

		BusinessApplicationHeaderV01 hdr = busMsg.getAppHdr();
		
		flatMsg.setFrBic(hdr.getFr().getFIId().getFinInstnId().getOthr().getId());
		
		flatMsg.setToBic(hdr.getTo().getFIId().getFinInstnId().getOthr().getId());
			
		flatMsg.setBizMsgIdr(hdr.getBizMsgIdr());
		
		flatMsg.setMsgDefIdr(hdr.getMsgDefIdr());

		if (!(null == hdr.getBizSvc()))
			flatMsg.setBizSvc(hdr.getBizSvc());
		
		flatMsg.setCreDt(strTgl(hdr.getCreDt()));

		if (!(null == hdr.getCpyDplct()))
			flatMsg.setCpyDplct(hdr.getCpyDplct().value());

		if (!(null == hdr.isPssblDplct()))
			flatMsg.setPssblDplct(hdr.isPssblDplct());
		
		FIToFICustomerCreditTransferV08 ct = busMsg.getDocument().getFiToFICstmrCdtTrf();
		
		flatMsg.setMsgId(ct.getGrpHdr().getMsgId()); 
		
		flatMsg.setCreDtTm(strTgl(ct.getGrpHdr().getCreDtTm()));
		
		flatMsg.setSettlementMtd(ct.getGrpHdr().getSttlmInf().getSttlmMtd().value());

		flatMsg.setEndToEndId(ct.getCdtTrfTxInf().get(0).getPmtId().getEndToEndId());
		
		if (!(null == ct.getCdtTrfTxInf().get(0).getPmtId().getTxId()))
			flatMsg.setTransactionId(ct.getCdtTrfTxInf().get(0).getPmtId().getTxId());
		
		if (!(null == ct.getCdtTrfTxInf().get(0).getPmtTpInf())) {
			
			if (!(null == ct.getCdtTrfTxInf().get(0).getPmtTpInf().getLclInstrm()))
				flatMsg.setPaymentChannel(ct.getCdtTrfTxInf().get(0).getPmtTpInf().getLclInstrm().getPrtry());
		
			if (!(null == ct.getCdtTrfTxInf().get(0).getPmtTpInf().getCtgyPurp()))
				flatMsg.setCategoryPurpose(ct.getCdtTrfTxInf().get(0).getPmtTpInf().getCtgyPurp().getPrtry().substring(3, 5));
		}
			
//		DecimalFormat df = new DecimalFormat("#############.00");
//		flatMsg.setAmount(df.format(ct.getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue()));	

		if (null != ct.getCdtTrfTxInf().get(0).getIntrBkSttlmAmt())
			flatMsg.setAmount(ct.getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue());	
				
		flatMsg.setCurrency(ct.getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getCcy());
		
		flatMsg.setChargeBearer(ct.getCdtTrfTxInf().get(0).getChrgBr().value());
		
		if (!(null == ct.getCdtTrfTxInf().get(0).getDbtr())) {
			
			if (!(null == ct.getCdtTrfTxInf().get(0).getDbtr().getNm())) 
				flatMsg.setDebtorName(ct.getCdtTrfTxInf().get(0).getDbtr().getNm());

			if (!(null == ct.getCdtTrfTxInf().get(0).getDbtr().getId())) {

				if (!(null == ct.getCdtTrfTxInf().get(0).getDbtr().getId().getOrgId()))
					flatMsg.setDebtorId(ct.getCdtTrfTxInf().get(0).getDbtr().getId().getOrgId().getOthr().get(0).getId());
	
				if (!(null == ct.getCdtTrfTxInf().get(0).getDbtr().getId().getPrvtId()))
					flatMsg.setDebtorId(ct.getCdtTrfTxInf().get(0).getDbtr().getId().getPrvtId().getOthr().get(0).getId());

			}
		}

		if (!(null == ct.getCdtTrfTxInf().get(0).getDbtrAcct())) {
			flatMsg.setDebtorAccountNo(ct.getCdtTrfTxInf().get(0).getDbtrAcct().getId().getOthr().getId());

			if (!(null == ct.getCdtTrfTxInf().get(0).getDbtrAcct().getTp()))
				flatMsg.setDebtorAccountType(ct.getCdtTrfTxInf().get(0).getDbtrAcct().getTp().getPrtry());
		}
		
		flatMsg.setDebtorAgentId(ct.getCdtTrfTxInf().get(0).getDbtrAgt().getFinInstnId().getOthr().getId());
		flatMsg.setCreditorAgentId(ct.getCdtTrfTxInf().get(0).getCdtrAgt().getFinInstnId().getOthr().getId());

		if (!(null == ct.getCdtTrfTxInf().get(0).getCdtr())) {
			
			if (!(null == ct.getCdtTrfTxInf().get(0).getCdtr().getNm())) 
				flatMsg.setCreditorName(ct.getCdtTrfTxInf().get(0).getCdtr().getNm());

			if (!(null == ct.getCdtTrfTxInf().get(0).getCdtr().getId())) {

				if (!(null == ct.getCdtTrfTxInf().get(0).getCdtr().getId().getOrgId()))
					flatMsg.setCreditorId(ct.getCdtTrfTxInf().get(0).getCdtr().getId().getOrgId().getOthr().get(0).getId());
	
				if (!(null == ct.getCdtTrfTxInf().get(0).getCdtr().getId().getPrvtId()))
					flatMsg.setCreditorId(ct.getCdtTrfTxInf().get(0).getCdtr().getId().getPrvtId().getOthr().get(0).getId());

			}
		}

		if (!(null == ct.getCdtTrfTxInf().get(0).getCdtrAcct())) {
			flatMsg.setCreditorAccountNo(ct.getCdtTrfTxInf().get(0).getCdtrAcct().getId().getOthr().getId());

			if (!(null == ct.getCdtTrfTxInf().get(0).getCdtrAcct().getTp()))
				flatMsg.setCreditorAccountType(ct.getCdtTrfTxInf().get(0).getCdtrAcct().getTp().getPrtry());
		}

		if (!(null == ct.getCdtTrfTxInf().get(0).getCdtrAcct().getPrxy())) {
			if (null != ct.getCdtTrfTxInf().get(0).getCdtrAcct().getPrxy().getTp())
				flatMsg.setCreditorAccountProxyType(ct.getCdtTrfTxInf().get(0).getCdtrAcct().getPrxy().getTp().getPrtry());
			if (null != ct.getCdtTrfTxInf().get(0).getCdtrAcct().getPrxy().getId())
				flatMsg.setCreditorAccountProxyType(ct.getCdtTrfTxInf().get(0).getCdtrAcct().getPrxy().getId());
		}

		if (!(null == ct.getCdtTrfTxInf().get(0).getRmtInf())) {
			if (ct.getCdtTrfTxInf().get(0).getRmtInf().getUstrd().size()>0)
				flatMsg.setPaymentInfo(ct.getCdtTrfTxInf().get(0).getRmtInf().getUstrd().get(0));
		}

		if (ct.getCdtTrfTxInf().get(0).getSplmtryData().size() > 0) {
			
			if (!(null == ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr())) {
				
				if (!(null == ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getTp() )) 
					flatMsg.setDebtorType(ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getTp());
			
				if (!(null == ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getRsdntSts() )) 
					flatMsg.setDebtorResidentialStatus(ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getRsdntSts());

				if (!(null == ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getTwnNm() )) 
					flatMsg.setDebtorTownName(ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().getTwnNm());
			}
			
			if (!(null == ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr())) {
				
				if (!(null == ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getTp() )) 
					flatMsg.setCreditorType(ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getTp());
			
				if (!(null == ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getRsdntSts() )) 
					flatMsg.setCreditorResidentialStatus(ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getRsdntSts());;

				if (!(null == ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getTwnNm() )) 
					flatMsg.setCreditorTownName(ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().getTwnNm());
			}

			flatMsg.setOrgnlEndToEndId("");
			if (null != ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getRltdEndToEndId()) {
				flatMsg.setOrgnlEndToEndId(ct.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getRltdEndToEndId());
			}
		}

		return flatMsg;
	}


	public FlatAdmi004Pojo flatteningAdmi004 (BusinessMessage busMsg) {
		FlatAdmi004Pojo admi004 = new FlatAdmi004Pojo();
				
		admi004.setFrBic(busMsg.getAppHdr().getFr().getFIId().getFinInstnId().getOthr().getId());	
		admi004.setToBic(busMsg.getAppHdr().getTo().getFIId().getFinInstnId().getOthr().getId());
		admi004.setBizMsgIdr(busMsg.getAppHdr().getBizMsgIdr());
		admi004.setMsgDefIdr(busMsg.getAppHdr().getMsgDefIdr());
		admi004.setBizSvc(busMsg.getAppHdr().getBizSvc());
		admi004.setCpyDplct(busMsg.getAppHdr().getCpyDplct().value());
		admi004.setCreDt(strTgl(busMsg.getAppHdr().getCreDt()));
		admi004.setPssblDplct(null);
		
		admi004.setEventCode(busMsg.getDocument().getSysEvtNtfctn().getEvtInf().getEvtCd());
		admi004.setEventDesc(busMsg.getDocument().getSysEvtNtfctn().getEvtInf().getEvtDesc());
		
		admi004.setEventParamList(busMsg.getDocument().getSysEvtNtfctn().getEvtInf().getEvtParam());
		
		admi004.setEventTime(strTgl(busMsg.getDocument().getSysEvtNtfctn().getEvtInf().getEvtTm()));
		
		return admi004;		
	}
	
	public FlatAdmi002Pojo flatteningAdmi002 (BusinessMessage busMsg) {
		FlatAdmi002Pojo admi002 = new FlatAdmi002Pojo();
				
		admi002.setFrBic(busMsg.getAppHdr().getFr().getFIId().getFinInstnId().getOthr().getId());	
		admi002.setToBic(busMsg.getAppHdr().getTo().getFIId().getFinInstnId().getOthr().getId());
		admi002.setBizMsgIdr(busMsg.getAppHdr().getBizMsgIdr());
		admi002.setMsgDefIdr(busMsg.getAppHdr().getMsgDefIdr());
		admi002.setBizSvc(busMsg.getAppHdr().getBizSvc());
		if (null!=busMsg.getAppHdr().getCpyDplct())
			admi002.setCpyDplct(busMsg.getAppHdr().getCpyDplct().value());
		admi002.setCreDt(strTgl(busMsg.getAppHdr().getCreDt()));
		admi002.setPssblDplct(null);
		
		admi002.setAdditionalData(busMsg.getDocument().getMessageReject().getRsn().getAddtlData());
		admi002.setErrorLocation(busMsg.getDocument().getMessageReject().getRsn().getErrLctn());
		admi002.setReasonDesc(busMsg.getDocument().getMessageReject().getRsn().getRsnDesc());
		admi002.setRejectDateTime(busMsg.getDocument().getMessageReject().getRsn().getRjctnDtTm().toString());
		admi002.setRejectReason(busMsg.getDocument().getMessageReject().getRsn().getRjctgPtyRsn());
		admi002.setRelatedRef(busMsg.getDocument().getMessageReject().getRltdRef().getRef());
		
		return admi002;		
	}
	
	
	public FlatPrxy901Pojo flatteningPrxy901 (BusinessMessage busMsg) {
		FlatPrxy901Pojo flat = new FlatPrxy901Pojo();
		
		flat.setBizMsgIdr(busMsg.getAppHdr().getBizMsgIdr());
		flat.setBizSvc(busMsg.getAppHdr().getBizSvc());
		flat.setCpyDplct(busMsg.getAppHdr().getCpyDplct().value());
		
		flat.setCreDtTm(strTgl(busMsg.getDocument().getPrxyNtfctn().getGrpHdr().getCreDtTm()));
		flat.setFrBic(busMsg.getAppHdr().getFr().getFIId().getFinInstnId().getOthr().getId());
		flat.setMsgDefIdr(busMsg.getAppHdr().getMsgDefIdr());
		flat.setMsgId(busMsg.getDocument().getPrxyNtfctn().getGrpHdr().getMsgId());
		
		flat.setOrgnlProxyValue(busMsg.getDocument().getPrxyNtfctn().getNtfctn().getOrgnlPrxy().getVal());
		flat.setOrgnlProxyType(busMsg.getDocument().getPrxyNtfctn().getNtfctn().getOrgnlPrxy().getTp());

		ProxyAccount1 newAccount = busMsg.getDocument().getPrxyNtfctn().getNtfctn().getNewAcct();
		flat.setNewAccountName(newAccount.getAcct().getNm());
		flat.setNewAccountNumber(newAccount.getAcct().getId().getOthr().getId());
		flat.setNewAccountType(newAccount.getAcct().getTp().getPrtry());
		flat.setNewBankId(newAccount.getAgt().getFinInstnId().getOthr().getId());
		flat.setNewDisplayName(newAccount.getDsplNm());
		flat.setNewRegistrationId(newAccount.getRegnId());

		if (newAccount.getSplmtryData().size()>0) {
			flat.setNewCustomerId(newAccount.getSplmtryData().get(0).getEnvlp().getDtl().getCstmr().getId());
			flat.setNewCustomerType(newAccount.getSplmtryData().get(0).getEnvlp().getDtl().getCstmr().getTp());
			flat.setNewResidentStatus(newAccount.getSplmtryData().get(0).getEnvlp().getDtl().getCstmr().getRsdntSts());
			flat.setNewTownName(newAccount.getSplmtryData().get(0).getEnvlp().getDtl().getCstmr().getTwnNm());
		}
		
		ProxyAccount1 orgnlAccount = busMsg.getDocument().getPrxyNtfctn().getNtfctn().getOrgnlAcct();
		flat.setOrgnlAccountName(orgnlAccount.getAcct().getNm());
		flat.setOrgnlAccountNumber(orgnlAccount.getAcct().getId().getOthr().getId());
		flat.setOrgnlAccountType(orgnlAccount.getAcct().getTp().getPrtry());
		flat.setOrgnlBankId(orgnlAccount.getAgt().getFinInstnId().getOthr().getId());
		flat.setOrgnlDisplayName(orgnlAccount.getDsplNm());
		flat.setOrgnlRegistrationId(orgnlAccount.getRegnId());

		if (orgnlAccount.getSplmtryData().size()>0) {
			flat.setOrgnlCustomerId(orgnlAccount.getSplmtryData().get(0).getEnvlp().getDtl().getCstmr().getId());
			flat.setOrgnlCustomerType(orgnlAccount.getSplmtryData().get(0).getEnvlp().getDtl().getCstmr().getTp());
			flat.setOrgnlResidentStatus(orgnlAccount.getSplmtryData().get(0).getEnvlp().getDtl().getCstmr().getRsdntSts());
			flat.setOrgnlTownName(orgnlAccount.getSplmtryData().get(0).getEnvlp().getDtl().getCstmr().getTwnNm());
		}
		
		flat.setRecipientBank(busMsg.getDocument().getPrxyNtfctn().getGrpHdr().getMsgRcpt().getAgt().getFinInstnId().getOthr().getId());
		flat.setToBic(busMsg.getAppHdr().getTo().getFIId().getFinInstnId().getOthr().getId());
		
		return flat;
	}
	
	private String strTgl (XMLGregorianCalendar tgl) {
		String strDate = String.format("%04d-%02d-%02dT%02d:%02d:%02d.%03d", 
				tgl.getYear(),tgl.getMonth(), tgl.getDay(),
				tgl.getHour(), tgl.getMinute(), tgl.getSecond(), tgl.getMillisecond());

		return strDate;
	}
	
}
