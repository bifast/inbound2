package bifast.inbound.iso20022;

import java.time.ZoneOffset;
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
public class AppHeaderService {

//	public BusinessApplicationHeaderV01 getAppHdr(String bicTo, String msgType, String bizMsgId) {
	public BusinessApplicationHeaderV01 getAppHdr(String msgType, String bizMsgId) {
		
        		
		BusinessApplicationHeaderV01 appHdr = new BusinessApplicationHeaderV01();
		
		Party9Choice fr = new Party9Choice();
		fr.setFIId(new BranchAndFinancialInstitutionIdentification5());
		fr.getFIId().setFinInstnId(new FinancialInstitutionIdentification8());
		fr.getFIId().getFinInstnId().setOthr(new GenericFinancialIdentification1());
		fr.getFIId().getFinInstnId().getOthr().setId("SIHBIDJ1");
		appHdr.setFr(fr);

		Party9Choice to = new Party9Choice();
		to.setFIId(new BranchAndFinancialInstitutionIdentification5());
		to.getFIId().setFinInstnId(new FinancialInstitutionIdentification8());
		to.getFIId().getFinInstnId().setOthr(new GenericFinancialIdentification1());
		to.getFIId().getFinInstnId().getOthr().setId("FASTIDJA");
		appHdr.setTo(to);

		appHdr.setMsgDefIdr(msgType);
		appHdr.setBizMsgIdr(bizMsgId);
		
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
		
		XMLGregorianCalendar xcal;
		try {
			xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			appHdr.setCreDt(xcal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		return appHdr;
	}



}
