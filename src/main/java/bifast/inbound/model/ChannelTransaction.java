package bifast.inbound.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;	

@Entity(name="KC_CHANNEL_TRANSACTION")
public class ChannelTransaction {

//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
//	@SequenceGenerator(name="seq_generator", sequenceName = "table_seq_generator", allocationSize=1)
//	private Long id;
//
	@Id
	@Column(length=20)
	private String komiTrnsId;

	@Column(length=20)
	private String channelRefId;

	@Column(length=15)
	private String channelId;
	
	@Column(length=100)
	private String msgName;
	
	private BigDecimal amount;

//	@Column(length=35)
//	private String debtorAccountNumber;
	
	@Column(length=8)
	private String recptBank;
	
//	@Column(length=35)
//	private String creditorAccountNumber;
	
	private String responseCode;
	
	private LocalDateTime requestTime;
	private Long elapsedTime;

	@Column(length=15)
	private String callStatus;
	@Column(length=250)
	private String errorMsg;
	
	@Column(length=1000)
	private String textMessage;
	
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
	public String getKomiTrnsId() {
		return komiTrnsId;
	}
	public void setKomiTrnsId(String komiTrnsId) {
		this.komiTrnsId = komiTrnsId;
	}
	public String getChannelRefId() {
		return channelRefId;
	}
	public void setChannelRefId(String channelRefId) {
		this.channelRefId = channelRefId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getMsgName() {
		return msgName;
	}
	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getRecptBank() {
		return recptBank;
	}
	public void setRecptBank(String recptBank) {
		this.recptBank = recptBank;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public LocalDateTime getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}
	public String getTextMessage() {
		return textMessage;
	}
	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}
	public Long getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
		
}
