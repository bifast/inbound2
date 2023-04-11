package bifast.inbound.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.datatype.DatatypeConfigurationException;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bifast.inbound.pojo.Pacs002Seed;
import bifast.library.iso20022.custom.BusinessMessage;
import bifast.library.iso20022.pacs002.CashAccount38;
import bifast.library.iso20022.pacs002.CashAccountType2Choice;
import bifast.library.iso20022.pacs002.FIToFIPaymentStatusReportV10;
import bifast.library.iso20022.pacs002.GenericAccountIdentification1;
import bifast.library.iso20022.pacs002.GroupHeader91;
import bifast.library.iso20022.pacs002.OriginalGroupHeader17;
import bifast.library.iso20022.pacs002.OriginalTransactionReference28;
import bifast.library.iso20022.pacs002.Party40Choice;
import bifast.library.iso20022.pacs002.PartyIdentification135;
import bifast.library.iso20022.pacs002.PaymentTransaction110;
import bifast.library.iso20022.pacs002.StatusReason6Choice;
import bifast.library.iso20022.pacs002.StatusReasonInformation12;
import bifast.library.iso20022.pacs002.SupplementaryDataEnvelope1;
import bifast.library.iso20022.pacs002.AccountIdentification4Choice;
import bifast.library.iso20022.pacs002.BIAddtlCstmrInf;
import bifast.library.iso20022.pacs002.BISupplementaryData1;
import bifast.library.iso20022.pacs002.BISupplementaryDataEnvelope1;

@Service
public class Pacs002MessageService {

	public FIToFIPaymentStatusReportV10 accountEnquiryResponse (Pacs002Seed seed, 
				BusinessMessage orgnlMessage) throws DatatypeConfigurationException {
		
		FIToFIPaymentStatusReportV10 pacs002 = new FIToFIPaymentStatusReportV10();

		// GrpHdr
		pacs002.setGrpHdr(new GroupHeader91());
		
		pacs002.getGrpHdr().setMsgId(seed.getMsgId());  // 510 transaction_type untuk Account ENquiry
		
//		GregorianCalendar gcal = new GregorianCalendar();
//		gcal.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
//		XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
//		pacs002.getGrpHdr().setCreDtTm(xcal);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.now();
		pacs002.getGrpHdr().setCreDtTm(fmt.format(localDateTime));

		
		// +OrgnlGrpInfAndSts
		pacs002.getOrgnlGrpInfAndSts().add(new OriginalGroupHeader17());
		pacs002.getOrgnlGrpInfAndSts().get(0).setOrgnlMsgId(orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getGrpHdr().getMsgId());
		pacs002.getOrgnlGrpInfAndSts().get(0).setOrgnlMsgNmId(orgnlMessage.getAppHdr().getMsgDefIdr());

		// TxInfAndSts
		PaymentTransaction110 txInfAndSts = new PaymentTransaction110();

		txInfAndSts.setOrgnlEndToEndId( orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getEndToEndId() );
		txInfAndSts.setOrgnlTxId(orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getTxId() );
		
		txInfAndSts.setTxSts(seed.getStatus());
		
		// TxInfAndSts / StsRsnInf
		txInfAndSts.getStsRsnInf().add(new StatusReasonInformation12());
		txInfAndSts.getStsRsnInf().get(0).setRsn(new StatusReason6Choice());

		txInfAndSts.getStsRsnInf().get(0).getRsn().setPrtry(seed.getReason());	
		
		// TxInfAndSts / OrgnlTxRef
		txInfAndSts.setOrgnlTxRef(new OriginalTransactionReference28());		
		txInfAndSts.getOrgnlTxRef().setIntrBkSttlmDt(orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmDt() );
	
		// TxInfAndSts / OrgnlTxRef / Cdtr
		
		if (!(null == seed.getCreditorName())) {
			txInfAndSts.getOrgnlTxRef().setCdtr(new Party40Choice());
			txInfAndSts.getOrgnlTxRef().getCdtr().setPty(new PartyIdentification135());
			txInfAndSts.getOrgnlTxRef().getCdtr().getPty().setNm(seed.getCreditorName());
		}
		
		// TxInfAndSts / OrgnlTxRef / CdtrAcct
		txInfAndSts.getOrgnlTxRef().setCdtrAcct(new CashAccount38());
		
		txInfAndSts.getOrgnlTxRef().getCdtrAcct().setId(new AccountIdentification4Choice());
		txInfAndSts.getOrgnlTxRef().getCdtrAcct().getId().setOthr(new GenericAccountIdentification1());
		txInfAndSts.getOrgnlTxRef().getCdtrAcct().getId().getOthr().setId(seed.getCreditorAccountNo());
		
		txInfAndSts.getOrgnlTxRef().getCdtrAcct().setTp(new CashAccountType2Choice());
		txInfAndSts.getOrgnlTxRef().getCdtrAcct().getTp().setPrtry(seed.getCreditorAccountIdType());
				
		// TxInfAndSts / SplmtryData

		if ((!(null==seed.getCreditorType())) ||
			(!(null==seed.getCreditorId())) ||
			(!(null==seed.getCreditorResidentialStatus())) ||
			(!(null==seed.getCreditorTown())) ) {

			txInfAndSts.getSplmtryData().add(new BISupplementaryData1());
			txInfAndSts.getSplmtryData().get(0).setEnvlp(new SupplementaryDataEnvelope1());
			txInfAndSts.getSplmtryData().get(0).getEnvlp().setDtl(new BISupplementaryDataEnvelope1());

			txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().setCdtr(new BIAddtlCstmrInf());
	
			if (null != seed.getCreditorType())
				txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().setTp(seed.getCreditorType());

			if (null != seed.getCreditorId())
				txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().setId(seed.getCreditorId());

			if (null != seed.getCreditorResidentialStatus())
				txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().setRsdntSts(seed.getCreditorResidentialStatus());
			
			if (null != seed.getCreditorTown())
				txInfAndSts.getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().setTwnNm(seed.getCreditorTown());

		}
		
		pacs002.getTxInfAndSts().add(txInfAndSts);
		
		return pacs002;
	}

	
	public FIToFIPaymentStatusReportV10 creditTransferRequestResponse (Pacs002Seed seed, 
							BusinessMessage orgnlMessage) throws DatatypeConfigurationException {
		
		FIToFIPaymentStatusReportV10 pacs002 = new FIToFIPaymentStatusReportV10();

		// GrpHdr
		GroupHeader91 grpHdr = new GroupHeader91();
		grpHdr.setMsgId(seed.getMsgId());  // 010 Transaction-Type untuk CSTMRCRDTTRN
		
//		GregorianCalendar gcal = new GregorianCalendar();
//		gcal.setTimeZone(TimeZone.getTimeZone(ZoneOffset.systemDefault()));
//		XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
//		grpHdr.setCreDtTm(xcal);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.now();
		grpHdr.setCreDtTm(fmt.format(localDateTime));

		pacs002.setGrpHdr(grpHdr);

		// OrgnlGrpInfAndSts
		OriginalGroupHeader17 OrgnlGrpInfAndSts = new OriginalGroupHeader17();
		OrgnlGrpInfAndSts.setOrgnlMsgId(orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getGrpHdr().getMsgId());
		OrgnlGrpInfAndSts.setOrgnlMsgNmId( orgnlMessage.getAppHdr().getMsgDefIdr() );
		
		pacs002.getOrgnlGrpInfAndSts().add(OrgnlGrpInfAndSts);
		
		// TxInfAndSts
		
		PaymentTransaction110 txInfAndSts = new PaymentTransaction110();
		
		txInfAndSts.setOrgnlEndToEndId( orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getEndToEndId() );
		txInfAndSts.setOrgnlTxId(orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getTxId() );
		txInfAndSts.setTxSts(seed.getStatus());
		
		// TxInfAndSts / StsRsnInf
		
		StatusReason6Choice rsn = new StatusReason6Choice();
		rsn.setPrtry(seed.getReason());
		StatusReasonInformation12 stsRsnInf = new StatusReasonInformation12();
		stsRsnInf.setRsn(rsn);
		
		if (!(null == seed.getAdditionalInfo())) {
			stsRsnInf.getAddtlInf().add(seed.getAdditionalInfo()); 
		}
		txInfAndSts.getStsRsnInf().add(stsRsnInf);
		
		// TxInfAndSts / OrgnlTxRef
		OriginalTransactionReference28 orgnlTxRef = new OriginalTransactionReference28();
		
		orgnlTxRef.setIntrBkSttlmDt(orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmDt() );

		// TxInfAndSts / OrgnlTxRef / Cdtr
		Party40Choice cdtr = new Party40Choice();
		if (!(null == seed.getCreditorName())) {
			PartyIdentification135 pty = new PartyIdentification135();
			pty.setNm(seed.getCreditorName());
			cdtr.setPty(pty);
		}
		orgnlTxRef.setCdtr(cdtr);
		txInfAndSts.setOrgnlTxRef(orgnlTxRef);
		
		// TxInfAndSts / SplmtryData
		BISupplementaryData1 splmtryData = new BISupplementaryData1();
		splmtryData.setEnvlp(new SupplementaryDataEnvelope1());
		splmtryData.getEnvlp().setDtl(new BISupplementaryDataEnvelope1());

		if ((!(null==seed.getCreditorType())) ||
				(!(null==seed.getCreditorId())) ||
				(!(null==seed.getCreditorResidentialStatus())) ||
				(!(null==seed.getCreditorTown())) ) {
				
			splmtryData.getEnvlp().getDtl().setCdtr(new BIAddtlCstmrInf());
			
			splmtryData.getEnvlp().getDtl().getCdtr().setTp(seed.getCreditorType());
			splmtryData.getEnvlp().getDtl().getCdtr().setId(seed.getCreditorId());
			splmtryData.getEnvlp().getDtl().getCdtr().setRsdntSts(seed.getCreditorResidentialStatus());
			splmtryData.getEnvlp().getDtl().getCdtr().setTwnNm(seed.getCreditorTown());

			txInfAndSts.getSplmtryData().add(splmtryData);
		}
		
		pacs002.getTxInfAndSts().add(txInfAndSts);
		
		return pacs002;
	}


	public FIToFIPaymentStatusReportV10 notFoundPaymentStatusResponse (Pacs002Seed seed, BusinessMessage orgnlMsg) 
			throws DatatypeConfigurationException {
		
		System.out.println("notfound PS");
		
		FIToFIPaymentStatusReportV10 pacs002 = new FIToFIPaymentStatusReportV10();
		
		// GrpHdr
		pacs002.setGrpHdr(new GroupHeader91());
		pacs002.getGrpHdr().setMsgId(seed.getMsgId()); 
		
//		GregorianCalendar gcal = new GregorianCalendar();
//		gcal.setTimeZone(TimeZone.getTimeZone(ZoneOffset.systemDefault()));
//		XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
//		pacs002.getGrpHdr().setCreDtTm(xcal);;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.now();
		pacs002.getGrpHdr().setCreDtTm(fmt.format(localDateTime));

		// OrgnlGrpInfAndSts
		pacs002.getOrgnlGrpInfAndSts().add(new OriginalGroupHeader17());
		pacs002.getOrgnlGrpInfAndSts().get(0).setOrgnlMsgId(orgnlMsg.getDocument().getFiToFIPmtStsReq().getGrpHdr().getMsgId());
		pacs002.getOrgnlGrpInfAndSts().get(0).setOrgnlMsgNmId(orgnlMsg.getAppHdr().getMsgDefIdr());
		
		// TxInfAndSts
		pacs002.getTxInfAndSts().add(new PaymentTransaction110());

		pacs002.getTxInfAndSts().get(0).setOrgnlEndToEndId(orgnlMsg.getDocument().getFiToFIPmtStsReq().getTxInf().get(0).getOrgnlEndToEndId());

		pacs002.getTxInfAndSts().get(0).setTxSts("OTHR");
		
		// TxInfAndSts / StsRsnInf	
		pacs002.getTxInfAndSts().get(0).getStsRsnInf().add(new StatusReasonInformation12());
		pacs002.getTxInfAndSts().get(0).getStsRsnInf().get(0).setRsn(new StatusReason6Choice());
		
		pacs002.getTxInfAndSts().get(0).getStsRsnInf().get(0).getRsn().setPrtry("U106");
			
		return pacs002;
	}
}
