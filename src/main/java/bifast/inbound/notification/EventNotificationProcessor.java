package bifast.inbound.notification;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.iso20022.AppHeaderService;
import bifast.inbound.pojo.Admi011Seed;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatAdmi004Pojo;
import bifast.inbound.service.Admi011MessageService;
import bifast.inbound.service.UtilService;
import bifast.library.iso20022.admi011.SystemEventAcknowledgementV01;
import bifast.library.iso20022.custom.BusinessMessage;
import bifast.library.iso20022.custom.Document;
import bifast.library.iso20022.head001.BusinessApplicationHeaderV01;

@Component
public class EventNotificationProcessor implements Processor{
	@Autowired private AppHeaderService appHdrService;
	@Autowired private Admi011MessageService admi011Service;

	@Autowired private UtilService utilService;

	@Override
	public void process(Exchange exchange) throws Exception {

		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
		FlatAdmi004Pojo flat = (FlatAdmi004Pojo) processData.getBiRequestFlat();
		
		String bizMsgId = utilService.genRfiBusMsgId("000", processData.getKomiTrnsId());
		String msgId = utilService.genMsgId("000", processData.getKomiTrnsId());

		Admi011Seed seed = new Admi011Seed();
		
		seed.setEventCode(flat.getEventCode());
		seed.setEventDateTime(flat.getCreDt());
		seed.setEventDesciption(flat.getEventDesc());
		seed.setEventParamList(flat.getEventParamList());
		seed.setMsgId(msgId);
//		seed.setOrgnlTrnsRef(null);
		
		SystemEventAcknowledgementV01 respMsg = admi011Service.acknowledge(seed);

		BusinessApplicationHeaderV01 appHdr = appHdrService.getAppHdr("admi.011.001.01", bizMsgId);
		
		Document doc = new Document();
		doc.setSysEvtAck(respMsg);

		BusinessMessage respBusMesg = new BusinessMessage();
		respBusMesg.setAppHdr(appHdr);
		respBusMesg.setDocument(doc);
		
		processData.setBiResponseMsg(respBusMesg);
		exchange.setProperty("prop_process_data", processData);
		exchange.getIn().setBody(respBusMesg);

	}

}
