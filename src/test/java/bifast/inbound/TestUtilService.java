package bifast.inbound;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import bifast.library.iso20022.custom.BusinessMessage;

@Service
public class TestUtilService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	public Integer getInboundCounter () {
		Random rand = new Random();
		return Integer.valueOf(rand.nextInt(2000));
	}

	public String genMessageId (String trxType, String bankCode) {
		String strToday = LocalDateTime.now().format(formatter);
		DecimalFormat df = new DecimalFormat("00000000");
		String strCounter = df.format(getInboundCounter());
		String msgId = strToday + bankCode + trxType + strCounter;
		return msgId;
	}

	public String genHubBusMsgId (String trxType) {
		String strToday = LocalDateTime.now().format(formatter);
		DecimalFormat df = new DecimalFormat("00000000");
		String strCounter = df.format(getInboundCounter());
		return strToday + "FASTIDJA" +  trxType + "H03" + strCounter;
	}
	
	public String genRfiBusMsgId (String trxType, String channel, String bankCode ) {
		String strToday = LocalDateTime.now().format(formatter);
		DecimalFormat df = new DecimalFormat("00000000");
		String strCounter = df.format(getInboundCounter());
		String msgId = strToday + bankCode + trxType + "R" + channel + strCounter;
		return msgId;
	}

	public String genBizMsgId (String oldId, String trxType) {
		String newId = oldId.substring(22, 30);
		String newDt = oldId.substring(0,8);
		return newDt + "SIHBIDJI" + trxType + "R99" + newId;
	}
	
	public String serializeBusinessMessage (BusinessMessage bm) throws JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JaxbAnnotationModule());
	    mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
	    mapper.setSerializationInclusion(Include.NON_NULL);
		String str = mapper.writeValueAsString(bm);
		return str;
	}

	public BusinessMessage deSerializeBusinessMessage (String str) throws JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JaxbAnnotationModule());
	    mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
	    mapper.setSerializationInclusion(Include.NON_NULL);
	    BusinessMessage bm = mapper.readValue(str, BusinessMessage.class);
		return bm;
	}

}
