package bifast.inbound.isoservice;

import java.time.ZoneOffset;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Service;

import bifast.library.iso20022.pacs008.AccountIdentification4Choice;
import bifast.library.iso20022.pacs008.ActiveCurrencyAndAmount;
import bifast.library.iso20022.pacs008.BIAddtlCstmrInf;
import bifast.library.iso20022.pacs008.BISupplementaryData1;
import bifast.library.iso20022.pacs008.BISupplementaryDataEnvelope1;
import bifast.library.iso20022.pacs008.BranchAndFinancialInstitutionIdentification6;
import bifast.library.iso20022.pacs008.CashAccount38;
import bifast.library.iso20022.pacs008.CashAccountType2Choice;
import bifast.library.iso20022.pacs008.CategoryPurpose1Choice;
import bifast.library.iso20022.pacs008.ChargeBearerType1Code;
import bifast.library.iso20022.pacs008.CreditTransferTransaction39;
import bifast.library.iso20022.pacs008.FIToFICustomerCreditTransferV08;
import bifast.library.iso20022.pacs008.FinancialInstitutionIdentification18;
import bifast.library.iso20022.pacs008.GenericAccountIdentification1;
import bifast.library.iso20022.pacs008.GenericFinancialIdentification1;
import bifast.library.iso20022.pacs008.GenericOrganisationIdentification1;
import bifast.library.iso20022.pacs008.GenericPersonIdentification1;
import bifast.library.iso20022.pacs008.GroupHeader93;
import bifast.library.iso20022.pacs008.LocalInstrument2Choice;
import bifast.library.iso20022.pacs008.OrganisationIdentification29;
import bifast.library.iso20022.pacs008.Party38Choice;
import bifast.library.iso20022.pacs008.PartyIdentification135;
import bifast.library.iso20022.pacs008.PaymentIdentification7;
import bifast.library.iso20022.pacs008.PaymentTypeInformation28;
import bifast.library.iso20022.pacs008.PersonIdentification13;
import bifast.library.iso20022.pacs008.ProxyAccountIdentification1;
import bifast.library.iso20022.pacs008.ProxyAccountType1Choice;
import bifast.library.iso20022.pacs008.RemittanceInformation16;
import bifast.library.iso20022.pacs008.SettlementInstruction7;
import bifast.library.iso20022.pacs008.SettlementMethod1Code;
import bifast.library.iso20022.pacs008.SupplementaryDataEnvelope1;

@Service
public class Pacs008MessageService {

	public FIToFICustomerCreditTransferV08 accountEnquiryRequest (Pacs008Seed seed) 
			throws DatatypeConfigurationException {
		
		FIToFICustomerCreditTransferV08 pacs008 = new FIToFICustomerCreditTransferV08();

		// GrpHdr
		GroupHeader93 grpHdr = new GroupHeader93();
		grpHdr.setMsgId(seed.getMsgId());
		
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTimeZone(TimeZone.getTimeZone(ZoneOffset.systemDefault()));
		XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		grpHdr.setCreDtTm(xcal);

		grpHdr.setNbOfTxs("1");

		SettlementInstruction7 sttlmInf =new SettlementInstruction7();
		sttlmInf.setSttlmMtd(SettlementMethod1Code.CLRG);
		grpHdr.setSttlmInf(sttlmInf);
		
		pacs008.setGrpHdr(grpHdr);

		// list of CdtTrfTxInf
		CreditTransferTransaction39 cdtTrfTxInf = new CreditTransferTransaction39();
		
		// CdtTrfTxInf / PmtId
		
		cdtTrfTxInf.setPmtId(new PaymentIdentification7());
		cdtTrfTxInf.getPmtId().setEndToEndId(seed.getBizMsgId());
		cdtTrfTxInf.getPmtId().setTxId(grpHdr.getMsgId());

		// CdtTrfTxInf / ++PmtTpInf / +++CtgyPurp / ++++Prtry
		
		cdtTrfTxInf.setPmtTpInf(new PaymentTypeInformation28());
		cdtTrfTxInf.getPmtTpInf().setCtgyPurp(new CategoryPurpose1Choice());
		cdtTrfTxInf.getPmtTpInf().getCtgyPurp().setPrtry(seed.getTrnType() + seed.getCategoryPurpose());

		// CdtTrfTxInf / IntrBkSttlmAmt

		ActiveCurrencyAndAmount ccyAmount = new ActiveCurrencyAndAmount();
		ccyAmount.setValue(seed.getAmount());
		ccyAmount.setCcy("IDR");
		cdtTrfTxInf.setIntrBkSttlmAmt(ccyAmount);
		
		// CdtTrfTxInf / ChrgBr
		cdtTrfTxInf.setChrgBr(ChargeBearerType1Code.DEBT);
		
		// CdtTrfTxInf / Dbtr
		PartyIdentification135 dbtr = new PartyIdentification135();
		cdtTrfTxInf.setDbtr(dbtr);
		
		// CdtTrfTxInf / DbtrAgt
		
		GenericFinancialIdentification1 dbtrInstnIdOthId  = new GenericFinancialIdentification1();
		dbtrInstnIdOthId.setId(seed.getOrignBank());
		FinancialInstitutionIdentification18 dbtrInstnId = new FinancialInstitutionIdentification18();
		dbtrInstnId.setOthr(dbtrInstnIdOthId);
		BranchAndFinancialInstitutionIdentification6 dbtrAgt = new BranchAndFinancialInstitutionIdentification6();
		dbtrAgt.setFinInstnId(dbtrInstnId);
		
		cdtTrfTxInf.setDbtrAgt(dbtrAgt);

		// CdtTrfTxInf / CdtrAgt
		GenericFinancialIdentification1 cdtrInstnIdOthId  = new GenericFinancialIdentification1();
		cdtrInstnIdOthId.setId(seed.getRecptBank());
		FinancialInstitutionIdentification18 cdtrInstnId = new FinancialInstitutionIdentification18();
		cdtrInstnId.setOthr(cdtrInstnIdOthId);
		BranchAndFinancialInstitutionIdentification6 cdtrAgt = new BranchAndFinancialInstitutionIdentification6();
		cdtrAgt.setFinInstnId(cdtrInstnId);

		cdtTrfTxInf.setCdtrAgt(cdtrAgt);
		
		// CdtTrfTxInf / Cdtr 
		PartyIdentification135 cdtr = new PartyIdentification135();
		cdtTrfTxInf.setCdtr(cdtr);
		
		// CdtTrfTxInf / CdtrAcct
		CashAccount38 cdtrAcct = new CashAccount38();

		GenericAccountIdentification1 cdtrAcctIdOthr = new GenericAccountIdentification1();
		cdtrAcctIdOthr.setId(seed.getCrdtAccountNo());
		AccountIdentification4Choice cdtrAcctId = new AccountIdentification4Choice();
		cdtrAcctId.setOthr(cdtrAcctIdOthr);
		cdtrAcct.setId(cdtrAcctId);

		cdtTrfTxInf.setCdtrAcct(cdtrAcct);
		
		// CdtTrfTxInf / SplmtryData
		
		pacs008.getCdtTrfTxInf().add(cdtTrfTxInf);
		
		return pacs008;
	}

	public FIToFICustomerCreditTransferV08 creditTransferRequest (Pacs008Seed seed) 
			throws DatatypeConfigurationException {
		
		FIToFICustomerCreditTransferV08 pacs008 = new FIToFICustomerCreditTransferV08();

		// GrpHdr
		pacs008.setGrpHdr(new GroupHeader93());
		pacs008.getGrpHdr().setMsgId(seed.getMsgId());
		
		GregorianCalendar gcal = new GregorianCalendar();
		XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		pacs008.getGrpHdr().setCreDtTm(xcal);

		pacs008.getGrpHdr().setNbOfTxs("1");
		
		pacs008.getGrpHdr().setSttlmInf(new SettlementInstruction7());
		pacs008.getGrpHdr().getSttlmInf().setSttlmMtd(SettlementMethod1Code.CLRG);
		
		// list of CdtTrfTxInf
		pacs008.getCdtTrfTxInf().add(new CreditTransferTransaction39());
		pacs008.getCdtTrfTxInf().get(0).setPmtId(new PaymentIdentification7());
		
		pacs008.getCdtTrfTxInf().get(0).getPmtId().setEndToEndId(seed.getBizMsgId());
		
		pacs008.getCdtTrfTxInf().get(0).getPmtId().setTxId(seed.getMsgId());
				
		
		// CdtTrfTxInf / PmtTpInf
		pacs008.getCdtTrfTxInf().get(0).setPmtTpInf(new PaymentTypeInformation28());

		pacs008.getCdtTrfTxInf().get(0).getPmtTpInf().setLclInstrm(new LocalInstrument2Choice());
		pacs008.getCdtTrfTxInf().get(0).getPmtTpInf().getLclInstrm().setPrtry(seed.getChannel());
		
		pacs008.getCdtTrfTxInf().get(0).getPmtTpInf().setCtgyPurp(new CategoryPurpose1Choice());
		if (null==seed.getCategoryPurpose()) 
			pacs008.getCdtTrfTxInf().get(0).getPmtTpInf().getCtgyPurp().setPrtry(seed.getTrnType() + "99");
		else
			pacs008.getCdtTrfTxInf().get(0).getPmtTpInf().getCtgyPurp().setPrtry(seed.getTrnType() + seed.getCategoryPurpose());

				
		// CdtTrfTxInf / IntrBkSttlmAmt
		pacs008.getCdtTrfTxInf().get(0).setIntrBkSttlmAmt(new ActiveCurrencyAndAmount());
		pacs008.getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().setCcy("IDR");
		pacs008.getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().setValue(seed.getAmount());
			
		// CdtTrfTxInf / ChrgBr
		pacs008.getCdtTrfTxInf().get(0).setChrgBr(ChargeBearerType1Code.DEBT);
		
		// CdtTrfTxInf / Dbtr
		pacs008.getCdtTrfTxInf().get(0).setDbtr(new PartyIdentification135());
		
		if (null!=seed.getDbtrName())
			pacs008.getCdtTrfTxInf().get(0).getDbtr().setNm(seed.getDbtrName());
		
		pacs008.getCdtTrfTxInf().get(0).getDbtr().setId(new Party38Choice());

		if (seed.getDbtrType().equals("01")) {
			pacs008.getCdtTrfTxInf().get(0).getDbtr().getId().setPrvtId(new PersonIdentification13());
			pacs008.getCdtTrfTxInf().get(0).getDbtr().getId().getPrvtId().getOthr().add(new GenericPersonIdentification1());
			pacs008.getCdtTrfTxInf().get(0).getDbtr().getId().getPrvtId().getOthr().get(0).setId(seed.getDbtrId());
		} 
		else {
			pacs008.getCdtTrfTxInf().get(0).getDbtr().getId().setOrgId(new OrganisationIdentification29());
			pacs008.getCdtTrfTxInf().get(0).getDbtr().getId().getOrgId().getOthr().add(new GenericOrganisationIdentification1());
			pacs008.getCdtTrfTxInf().get(0).getDbtr().getId().getOrgId().getOthr().get(0).setId(seed.getDbtrId());
		}
			
		// CdtTrfTxInf / DbtrAcct
		pacs008.getCdtTrfTxInf().get(0).setDbtrAcct(new CashAccount38());
		pacs008.getCdtTrfTxInf().get(0).getDbtrAcct().setId(new AccountIdentification4Choice());
		pacs008.getCdtTrfTxInf().get(0).getDbtrAcct().getId().setOthr(new GenericAccountIdentification1());
		
		pacs008.getCdtTrfTxInf().get(0).getDbtrAcct().getId().getOthr().setId(seed.getDbtrAccountNo());
		
		pacs008.getCdtTrfTxInf().get(0).getDbtrAcct().setTp(new CashAccountType2Choice());
		pacs008.getCdtTrfTxInf().get(0).getDbtrAcct().getTp().setPrtry(seed.getDbtrAccountType());
		
		// CdtTrfTxInf / DbtrAgt
		pacs008.getCdtTrfTxInf().get(0).setDbtrAgt(new BranchAndFinancialInstitutionIdentification6());
		pacs008.getCdtTrfTxInf().get(0).getDbtrAgt().setFinInstnId(new FinancialInstitutionIdentification18());
		pacs008.getCdtTrfTxInf().get(0).getDbtrAgt().getFinInstnId().setOthr(new GenericFinancialIdentification1());
		
		pacs008.getCdtTrfTxInf().get(0).getDbtrAgt().getFinInstnId().getOthr().setId(seed.getOrignBank());
		
		// CdtTrfTxInf / CdtrAgt
		
		pacs008.getCdtTrfTxInf().get(0).setCdtrAgt(new BranchAndFinancialInstitutionIdentification6());
		pacs008.getCdtTrfTxInf().get(0).getCdtrAgt().setFinInstnId(new FinancialInstitutionIdentification18());
		pacs008.getCdtTrfTxInf().get(0).getCdtrAgt().getFinInstnId().setOthr(new GenericFinancialIdentification1());

		pacs008.getCdtTrfTxInf().get(0).getCdtrAgt().getFinInstnId().getOthr().setId(seed.getRecptBank());
			
		// CdtTrfTxInf / Cdtr 
		pacs008.getCdtTrfTxInf().get(0).setCdtr(new PartyIdentification135());
		
		if (!(null==seed.getCrdtName()))
			pacs008.getCdtTrfTxInf().get(0).getCdtr().setNm(seed.getCrdtName());
		
		if (!(null!=seed.getCrdtId()) || (!(seed.getCrdtId().isBlank()))) {
			pacs008.getCdtTrfTxInf().get(0).getCdtr().setId(new Party38Choice());
		
			if (null != seed.getCrdtType()) {
				if (seed.getCrdtType().equals("01")) {
					pacs008.getCdtTrfTxInf().get(0).getCdtr().getId().setPrvtId(new PersonIdentification13());
					pacs008.getCdtTrfTxInf().get(0).getCdtr().getId().getPrvtId().getOthr().add(new GenericPersonIdentification1());
					pacs008.getCdtTrfTxInf().get(0).getCdtr().getId().getPrvtId().getOthr().get(0).setId(seed.getCrdtId());
				}
				else {
					pacs008.getCdtTrfTxInf().get(0).getCdtr().getId().setOrgId(new OrganisationIdentification29());
					pacs008.getCdtTrfTxInf().get(0).getCdtr().getId().getOrgId().getOthr().add(new GenericOrganisationIdentification1());
					pacs008.getCdtTrfTxInf().get(0).getCdtr().getId().getOrgId().getOthr().get(0).setId(seed.getCrdtId());
				} 
			}
		
		}
		
		// CdtTrfTxInf / CdtrAcct
		pacs008.getCdtTrfTxInf().get(0).setCdtrAcct(new CashAccount38());
		pacs008.getCdtTrfTxInf().get(0).getCdtrAcct().setId(new AccountIdentification4Choice());
		pacs008.getCdtTrfTxInf().get(0).getCdtrAcct().getId().setOthr(new GenericAccountIdentification1());
		
		pacs008.getCdtTrfTxInf().get(0).getCdtrAcct().getId().getOthr().setId(seed.getCrdtAccountNo());
		
		pacs008.getCdtTrfTxInf().get(0).getCdtrAcct().setTp(new CashAccountType2Choice());
		pacs008.getCdtTrfTxInf().get(0).getCdtrAcct().getTp().setPrtry(seed.getCrdtAccountType());
		
		if (null != seed.getCrdtProxyIdValue()) {
			pacs008.getCdtTrfTxInf().get(0).getCdtrAcct().setPrxy(new ProxyAccountIdentification1());
			
			pacs008.getCdtTrfTxInf().get(0).getCdtrAcct().getPrxy().setId(seed.getCrdtProxyIdValue());
			
			pacs008.getCdtTrfTxInf().get(0).getCdtrAcct().getPrxy().setTp(new ProxyAccountType1Choice());
			pacs008.getCdtTrfTxInf().get(0).getCdtrAcct().getPrxy().getTp().setPrtry(seed.getCrdtProxyIdType());
		}
			

		// CdtTrfTxInf / RmtInf
		pacs008.getCdtTrfTxInf().get(0).setRmtInf(new RemittanceInformation16());
		if (!(null==seed.getPaymentInfo())) {
			pacs008.getCdtTrfTxInf().get(0).getRmtInf().getUstrd().add(seed.getPaymentInfo());
		}
		
		// CdtTrfTxInf / SplmtryData
		// unt credit transfer tidak digunakan
		pacs008.getCdtTrfTxInf().get(0).getSplmtryData().add(new BISupplementaryData1());
		pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).setEnvlp(new SupplementaryDataEnvelope1());
		pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().setDtl(new BISupplementaryDataEnvelope1() );
		
		
		
		if ((null != seed.getCrdtType()) ||
			(null != seed.getCrdtResidentStatus()) ||
			(null != seed.getCrdtTownName()) 
			) {
			
			pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().setCdtr(new BIAddtlCstmrInf());
						
			if (!(null==seed.getCrdtType())) 
				pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().setTp(seed.getCrdtType());
			if (!(null==seed.getCrdtResidentStatus())) 
				pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().setRsdntSts(seed.getCrdtResidentStatus());
			if (!(null==seed.getCrdtTownName())) 
				pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getCdtr().setTwnNm(seed.getCrdtTownName());
			
		}

		if ((null != seed.getDbtrType()) ||
			(null != seed.getDbtrResidentStatus()) ||
			(null != seed.getDbtrTownName()) 
			) {
				
			pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().setDbtr(new BIAddtlCstmrInf());
			
			if (!(null==seed.getDbtrType())) 
				pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().setTp(seed.getDbtrType());
			if (!(null==seed.getDbtrResidentStatus())) 
				pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().setRsdntSts(seed.getDbtrResidentStatus());
			if (!(null==seed.getDbtrTownName())) 
				pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().getDbtr().setTwnNm(seed.getDbtrTownName());
			
		}
		
		if (null != seed.getEndToEndId())
			pacs008.getCdtTrfTxInf().get(0).getSplmtryData().get(0).getEnvlp().getDtl().setRltdEndToEndId(seed.getEndToEndId());
		
		return pacs008;

	}

	public FIToFICustomerCreditTransferV08 reverseCreditTransferRequest (Pacs008Seed seed) 
			throws DatatypeConfigurationException {
		
		FIToFICustomerCreditTransferV08 rctRequest = creditTransferRequest(seed);
		
		rctRequest.getCdtTrfTxInf().get(0).getPmtId().setTxId(seed.getEndToEndId());
		return rctRequest;
		
	}


}
