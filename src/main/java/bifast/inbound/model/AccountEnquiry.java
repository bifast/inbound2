package bifast.inbound.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="KC_ACCOUNT_ENQUIRY")
public class AccountEnquiry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
	@SequenceGenerator(name="seq_generator", sequenceName = "table_seq_generator", allocationSize=1)
	private Long id;
	
	@Column(length=20)
	private String komiTrnsId;
	
	@Column(length=20)
	private String chnlRefId;
	
	@Column(name="REQ_BIZMSGID", length=50)
	private String reqBizMsgIdr;

	@Column(name="RESP_BIZMSGID", length=50)
	private String respBizMsgIdr;

	@Column(name="ORIGN_BANK", length=20)
	private String originatingBank;
	@Column(name="RECPT_BANK", length=20)
	private String recipientBank;

	private BigDecimal amount;
	
	@Column(length=50)
	private String accountNo;

	@Column(name="CIHUB_REQ_TIME")
	private LocalDateTime submitDt;
		
	private Long elapsedTime;
	
	@Column(name="FULL_REQUEST_MSG", length=4000)
	private String fullRequestMessage;

	@Column(name="FULL_RESPONSE_MSG", length=4000)
	private String fullResponseMsg;
	
	@Column(length=20)
	private String callStatus;
	@Column(length=20)
	private String responseCode;
	@Column(length=20)
	private String reasonCode;
	
	@Column(length=400)
	private String errorMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKomiTrnsId() {
		return komiTrnsId;
	}

	public void setKomiTrnsId(String komiTrnsId) {
		this.komiTrnsId = komiTrnsId;
	}

	public String getChnlRefId() {
		return chnlRefId;
	}

	public void setChnlRefId(String chnlRefId) {
		this.chnlRefId = chnlRefId;
	}

	public String getReqBizMsgIdr() {
		return reqBizMsgIdr;
	}

	public void setReqBizMsgIdr(String reqBizMsgIdr) {
		this.reqBizMsgIdr = reqBizMsgIdr;
	}

	public String getRespBizMsgIdr() {
		return respBizMsgIdr;
	}

	public void setRespBizMsgIdr(String respBizMsgIdr) {
		this.respBizMsgIdr = respBizMsgIdr;
	}

	public String getOriginatingBank() {
		return originatingBank;
	}

	public void setOriginatingBank(String originatingBank) {
		this.originatingBank = originatingBank;
	}

	public String getRecipientBank() {
		return recipientBank;
	}

	public void setRecipientBank(String recipientBank) {
		this.recipientBank = recipientBank;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public LocalDateTime getSubmitDt() {
		return submitDt;
	}

	public void setSubmitDt(LocalDateTime submitDt) {
		this.submitDt = submitDt;
	}

	public Long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getFullRequestMessage() {
		return fullRequestMessage;
	}

	public void setFullRequestMessage(String fullRequestMessage) {
		this.fullRequestMessage = fullRequestMessage;
	}

	public String getFullResponseMsg() {
		return fullResponseMsg;
	}

	public void setFullResponseMsg(String fullResponseMsg) {
		this.fullResponseMsg = fullResponseMsg;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}



}
