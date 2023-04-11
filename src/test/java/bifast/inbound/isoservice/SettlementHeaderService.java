package bifast.inbound.isoservice;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Service;

import bifast.library.iso20022.head001.BranchAndFinancialInstitutionIdentification5;
import bifast.library.iso20022.head001.BusinessApplicationHeaderV01;
import bifast.library.iso20022.head001.FinancialInstitutionIdentification8;
import bifast.library.iso20022.head001.GenericFinancialIdentification1;
import bifast.library.iso20022.head001.Party9Choice;



@Service
public class SettlementHeaderService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	public BusinessApplicationHeaderV01 getAppHdr(String msgType, String bizMsgId) {
		
        		
		BusinessApplicationHeaderV01 appHdr = new BusinessApplicationHeaderV01();
		
		appHdr.setFr(new Party9Choice());
		appHdr.getFr().setFIId(new BranchAndFinancialInstitutionIdentification5());
		appHdr.getFr().getFIId().setFinInstnId(new FinancialInstitutionIdentification8());
		appHdr.getFr().getFIId().getFinInstnId().setOthr(new GenericFinancialIdentification1());
		appHdr.getFr().getFIId().getFinInstnId().getOthr().setId("INDOIDJA");
		
		appHdr.setTo(new Party9Choice());
		appHdr.getTo().setFIId(new BranchAndFinancialInstitutionIdentification5());
		appHdr.getTo().getFIId().setFinInstnId(new FinancialInstitutionIdentification8());
		appHdr.getTo().getFIId().getFinInstnId().setOthr(new GenericFinancialIdentification1());
		appHdr.getTo().getFIId().getFinInstnId().getOthr().setId("SIHBIDJI");

		appHdr.setMsgDefIdr("pacs.002.001.10");
		appHdr.setBizMsgIdr(bizMsgId);	
		appHdr.setBizSvc("STTL");
			
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
		
		XMLGregorianCalendar xcal;
		try {
//			gcal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
			xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			appHdr.setCreDt(xcal);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return appHdr;
	}



}
