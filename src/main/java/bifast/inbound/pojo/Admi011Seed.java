package bifast.inbound.pojo;

import java.util.List;

public class Admi011Seed {

	private String msgId;
	private String orgnlTrnsRef;   
	private String eventCode; 
	private List<String> eventParamList;
	
	private String eventDesciption; 
	private String eventDateTime;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getOrgnlTrnsRef() {
		return orgnlTrnsRef;
	}
	public void setOrgnlTrnsRef(String orgnlTrnsRef) {
		this.orgnlTrnsRef = orgnlTrnsRef;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public List<String> getEventParamList() {
		return eventParamList;
	}
	public void setEventParamList(List<String> eventParamList) {
		this.eventParamList = eventParamList;
	}
	public String getEventDesciption() {
		return eventDesciption;
	}
	public void setEventDesciption(String eventDesciption) {
		this.eventDesciption = eventDesciption;
	}
	public String getEventDateTime() {
		return eventDateTime;
	}
	public void setEventDateTime(String eventDateTime) {
		this.eventDateTime = eventDateTime;
	} 
	

	
}
