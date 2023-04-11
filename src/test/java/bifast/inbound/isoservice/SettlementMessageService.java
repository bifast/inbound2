package bifast.inbound.isoservice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bifast.library.iso20022.custom.BusinessMessage;
import bifast.library.iso20022.pacs002.CashAccount38;
import bifast.library.iso20022.pacs002.CashAccountType2Choice;
import bifast.library.iso20022.pacs002.FIToFIPaymentStatusReportV10;
import bifast.library.iso20022.pacs002.FinancialInstitutionIdentification18;
import bifast.library.iso20022.pacs002.GenericAccountIdentification1;
import bifast.library.iso20022.pacs002.GenericFinancialIdentification1;
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
import bifast.library.iso20022.pacs002.BranchAndFinancialInstitutionIdentification6;

@Service
public class SettlementMessageService {


	public FIToFIPaymentStatusReportV10 SettlementConfirmation (String msgId, 
							BusinessMessage orgnlMessage) throws DatatypeConfigurationException {
		
		FIToFIPaymentStatusReportV10 pacs002 = new FIToFIPaymentStatusReportV10();

		// GrpHdr
		GroupHeader91 grpHdr = new GroupHeader91();
		grpHdr.setMsgId(msgId);  // 010 Transaction-Type untuk CSTMRCRDTTRN
		
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
		
		pacs002.getTxInfAndSts().add(new PaymentTransaction110());
		
		pacs002.getTxInfAndSts().get(0).setOrgnlEndToEndId(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getEndToEndId() );
		pacs002.getTxInfAndSts().get(0).setOrgnlTxId(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getTxId() );
		pacs002.getTxInfAndSts().get(0).setTxSts("ACSC");
		
		// TxInfAndSts / StsRsnInf
		
		pacs002.getTxInfAndSts().get(0).getStsRsnInf().add(new StatusReasonInformation12());
		pacs002.getTxInfAndSts().get(0).getStsRsnInf().get(0).setRsn(new StatusReason6Choice());
		pacs002.getTxInfAndSts().get(0).getStsRsnInf().get(0).getRsn().setPrtry("U000");
		
		pacs002.getTxInfAndSts().get(0).getStsRsnInf().get(0).getAddtlInf().add("OK");
		
		
		// TxInfAndSts / OrgnlTxRef
		pacs002.getTxInfAndSts().get(0).setOrgnlTxRef(new OriginalTransactionReference28());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().setIntrBkSttlmDt(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmDt());

		// TxInfAndSts / OrgnlTxRef / Dbtr
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().setDbtr(new Party40Choice());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtr().setPty(new PartyIdentification135());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtr().getPty().setNm(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getDbtr().getNm());

		// TxInfAndSts / OrgnlTxRef / +++DbtrAcct
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().setDbtrAcct(new CashAccount38());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtrAcct().setId(new AccountIdentification4Choice());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtrAcct().getId().setOthr(new GenericAccountIdentification1());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtrAcct().getId().getOthr().setId(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getDbtrAcct().getId().getOthr().getId());
		
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtrAcct().setTp(new CashAccountType2Choice ());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtrAcct().getTp().setPrtry(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getDbtrAcct().getTp().getPrtry()	);
		
		// TxInfAndSts / OrgnlTxRef / +++DbtrAgt
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().setDbtrAgt(new BranchAndFinancialInstitutionIdentification6());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtrAgt().setFinInstnId(new FinancialInstitutionIdentification18());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtrAgt().getFinInstnId().setOthr(new GenericFinancialIdentification1());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getDbtrAgt().getFinInstnId().getOthr().setId(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getDbtrAgt().getFinInstnId().getOthr().getId() );
		
		// TxInfAndSts / OrgnlTxRef / +++CdtrAgt
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().setCdtrAgt(new BranchAndFinancialInstitutionIdentification6());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtrAgt().setFinInstnId(new FinancialInstitutionIdentification18());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtrAgt().getFinInstnId().setOthr(new GenericFinancialIdentification1());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtrAgt().getFinInstnId().getOthr().setId(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getCdtrAgt().getFinInstnId().getOthr().getId() );

		// TxInfAndSts / OrgnlTxRef / Cdtr +++++Nm
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().setCdtr(new Party40Choice());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtr().setPty(new PartyIdentification135());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtr().getPty().setNm(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getCdtr().getNm());
		
		// TxInfAndSts / OrgnlTxRef / +++CdtrAcct

		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().setCdtrAcct(new CashAccount38());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtrAcct().setId(new AccountIdentification4Choice());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtrAcct().getId().setOthr(new GenericAccountIdentification1());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtrAcct().getId().getOthr().setId(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getCdtrAcct().getId().getOthr().getId());

		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtrAcct().setTp(new CashAccountType2Choice ());
		pacs002.getTxInfAndSts().get(0).getOrgnlTxRef().getCdtrAcct().getTp().setPrtry(
				orgnlMessage.getDocument().getFiToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getCdtrAcct().getTp().getPrtry()	);

		// TxInfAndSts ++SplmtryData	+++Envlp++++Dtl	
		pacs002.getTxInfAndSts().get(0).getSplmtryData().add(new BISupplementaryData1());
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).setEnvlp(new SupplementaryDataEnvelope1());
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).getEnvlp().setDtl(new BISupplementaryDataEnvelope1());
		
		// TxInfAndSts /.../ +++++DbtrAgtAcct++++++Id+++++++Othr	++++++++Id
		
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).getEnvlp().getDtl().setDbtrAgtAcct(new CashAccount38());
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtrAgtAcct().setId(new AccountIdentification4Choice());
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtrAgtAcct().getId().setOthr(new GenericAccountIdentification1());
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtrAgtAcct().getId().getOthr().setId("01234567");
		
		// TxInfAndSts /.../ +++++CdtrAgtAcct++++++Id+++++++Othr	++++++++Id
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).getEnvlp().getDtl().setCdtrAgtAcct(new CashAccount38());
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtrAgtAcct().setId(new AccountIdentification4Choice());
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtrAgtAcct().getId().setOthr(new GenericAccountIdentification1());
		pacs002.getTxInfAndSts().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtrAgtAcct().getId().getOthr().setId("567890");

		
		// TxInfAndSts / SplmtryData

//		if ((!(null==seed.getCreditorType())) ||
//				(!(null==seed.getCreditorId())) ||
//				(!(null==seed.getCreditorResidentialStatus())) ||
//				(!(null==seed.getCreditorTown())) ) {
//				
//			splmtryData.getEnvlp().getDtl().setCdtr(new BIAddtlCstmrInf());
//			
//			splmtryData.getEnvlp().getDtl().getCdtr().setTp(seed.getCreditorType());
//			splmtryData.getEnvlp().getDtl().getCdtr().setId(seed.getCreditorId());
//			splmtryData.getEnvlp().getDtl().getCdtr().setRsdntSts(seed.getCreditorResidentialStatus());
//			splmtryData.getEnvlp().getDtl().getCdtr().setTwnNm(seed.getCreditorTown());
//
//			txInfAndSts.getSplmtryData().add(splmtryData);
//		}
		
		
		return pacs002;
	}


}
