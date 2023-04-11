package bifast.inbound.service;

import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Service;

import bifast.inbound.pojo.Admi011Seed;
import bifast.library.iso20022.admi011.Event1;
import bifast.library.iso20022.admi011.SystemEventAcknowledgementV01;

@Service
public class Admi011MessageService {
	
	public SystemEventAcknowledgementV01 acknowledge (Admi011Seed seed) 
			throws DatatypeConfigurationException {
		
		SystemEventAcknowledgementV01 admi011 = new SystemEventAcknowledgementV01();
		
		admi011.setMsgId(seed.getMsgId());
		
		admi011.setOrgtrRef(seed.getOrgnlTrnsRef());

		admi011.setAckDtls(new Event1());
		
		admi011.getAckDtls().setEvtCd(seed.getEventCode());
		admi011.getAckDtls().setEvtDesc(seed.getEventDesciption());
		admi011.getAckDtls().getEvtParam().addAll(seed.getEventParamList());
		
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
		XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		admi011.getAckDtls().setEvtTm(xcal);

		return admi011;
				
	}
	
		
}
