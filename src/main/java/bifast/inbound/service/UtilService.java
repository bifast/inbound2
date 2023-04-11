package bifast.inbound.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.camel.MessageHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import bifast.inbound.config.Config;
import bifast.library.iso20022.custom.BusinessMessage;

@Service
public class UtilService {

	@Autowired	private Config config;

	DecimalFormat df = new DecimalFormat("00000000");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

//	public String genKomiTrnsId () {
//		String strToday = LocalDate.now().format(formatter);
//		Long cnt = cbTransactionRepository.getKomiSequence();
//		return (strToday + "R" + df.format(cnt));
//	}

	public String genRfiBusMsgId (String trxType, String komiTrnsId) {

		String komiId = komiTrnsId.substring(9);
		String strToday = LocalDateTime.now().format(formatter);
		String msgId = strToday + config.getBankcode() + trxType + "R99" +komiId;
		return msgId;
	}

	public String genOfiBusMsgId (String trxType, String komiTrnsId) {

		String komiId = komiTrnsId.substring(9);
		String strToday = LocalDateTime.now().format(formatter);
		String msgId = strToday + config.getBankcode() + trxType + "O99" +komiId;
		return msgId;
	}

	public String genMsgId (String trxType, String komiTrnsId) {
		String strToday = LocalDateTime.now().format(formatter);
		String komiId = komiTrnsId.substring(9);
		String msgId = strToday + config.getBankcode() + trxType + komiId;
		return msgId;
	}
	
	public long getElapsedFromMessageHistory (List<MessageHistory> list, String nodeId) {
		long elapsed = -1;
		for (MessageHistory msg : list) {
			if (nodeId == msg.getNode().getId())
				elapsed = msg.getElapsed();
		}
		return elapsed;
	}

	public String serializeBusinessMassage (BusinessMessage bm) throws JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JaxbAnnotationModule());
	    mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
	    mapper.setSerializationInclusion(Include.NON_NULL);
		String str = mapper.writeValueAsString(bm);
		return str;
	}

//	public LocalDateTime getTimestampFromMessageHistory (List<MessageHistory> list, String nodeId) {
//		LocalDateTime ldt = LocalDateTime.now();
//		for (MessageHistory msg : list) {
//			if (nodeId == msg.getNode().getId()) {
//				long time = msg.getTime();
//				ldt = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
//			}
//		}
//
//		return ldt;
//	}

//	public long getRouteElapsed (List<MessageHistory> messageList, String routeId) {
//		long start = 0;
//		long end = 0;
//
//		for (MessageHistory msg : messageList) {
//
//			if ( (msg.getRouteId().equals(routeId)) && 
//				 (msg.getNode().getId().equals("start_route")) ) {
//				start = msg.getTime();
//			}
//			if ( (msg.getRouteId().equals(routeId)) && 
//					 (msg.getNode().getId().equals("end_route")) ) {
//				end = msg.getTime();
//				}
//		}
//
//		return (end-start);
//	}


	public String getMsgType (String bizDefIdr, String bizMsgIdr) {
		
		String msgType = "";
		String code = bizMsgIdr.substring(16,19);
		
		if (bizDefIdr.startsWith("pacs.002")) {
			if (code.equals("510"))
				msgType = "AEResp";
			if (code.equals("010"))
				msgType = "CTResp";
			if (code.equals("110"))
				msgType = "CTResp";
			else if (code.equals("011"))
				msgType = "RevCTResp";
		}
		
		else if (bizDefIdr.startsWith("pacs.008")) {
			if (code.equals("510"))
				msgType = "AEReq";
			else if (code.equals("010"))
				msgType = "CTReq";
			else if (code.equals("110"))
				msgType = "CTReq";
			else if (code.equals("011"))
				msgType = "RevCTReq";
		}
			
		else if (bizDefIdr.startsWith("pacs.028"))   
			msgType = "PSReq";

		else if (bizDefIdr.startsWith("prxy.001"))   
			msgType = "PxRegReq";
		
		else if (bizDefIdr.startsWith("prxy.002"))   
			msgType = "PxRegResp";

		else if (bizDefIdr.startsWith("prxy.003"))   
			msgType = "PxResReq";
		
		else if (bizDefIdr.startsWith("prxy.004"))   
			msgType = "PxRsltResp";

		else if (bizDefIdr.startsWith("admi.002"))   
			msgType = "RjctResp";

		return msgType;
	}
}
