package bifast.inbound.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="KC_CREDIT_TRANSFER")
public class CreditTransfer {

	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
	@SequenceGenerator(name="seq_generator", sequenceName = "table_seq_generator", allocationSize=1)
	private Long id;
	
	@Column(length=50)
	private String komiTrnsId;
	
	@Column(length=50)
	private String msgType;
	
	@Column(name="ORIGN_BANK", length=10)
	private String originatingBank;
	@Column(name="RECPT_BANK", length=10)
	private String recipientBank;

	@Column(name="DEBTOR_ACCT_NO", length=50)
	private String debtorAccountNumber;
	@Column(name="DEBTOR_ACCT_TYPE", length=10)
	private String debtorAccountType;

	@Column(length=50)
	private String debtorId;
	@Column(length=10)
	private String debtorType;
	
	@Column(name="CREDITOR_ACCT_NO", length=50)
	private String creditorAccountNumber;
	@Column(name="CREDITOR_ACCT_TYPE", length=10)
	private String creditorAccountType;
	@Column(name="CREDITOR_ID", length=50)
	private String creditorId;
	@Column(length=10)
	private String creditorType;
	
	private BigDecimal amount;	
	
	@Column(name="REQ_BIZMSGID", length=50)
	private String crdtTrnRequestBizMsgIdr;

	@Column(name="E2E_ID", length=50)
	private String endToEndId;

	@Column(name="RESP_BIZMSGID", length=50)
	private String crdtTrnResponseBizMsgIdr;

	@Column(name="STTL_BIZMSGID", length=50)
	private String settlementConfBizMsgIdr;

	@Column(name="CIHUB_REQ_TIME")
	private LocalDateTime cihubRequestDT;
	
	private Long cihubElapsedTime;
	
	@Column(name="FULL_REQUEST_MSG", length=4000)
	private String fullRequestMessage;

	@Column(name="FULL_RESPONSE_MSG", length=4000)
	private String fullResponseMsg;
	
	@Column(length=400)
	private String errorMessage;
	
	private Integer psCounter;
	
	@Column(name="CPYDPLCT", length=10)
	private String cpyDplct;
		
	@Column(length=10)
	private String reversal;

	@Column(length=20)
	private String callStatus;
	
	@Column(length=20)
	private String reasonCode;
	@Column(length=20)
	private String responseCode;
	@Column(length=20)
	private String cbStatus;
	
	private LocalDateTime createDt;
	private LocalDateTime lastUpdateDt;
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
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
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
	public String getDebtorAccountNumber() {
		return debtorAccountNumber;
	}
	public void setDebtorAccountNumber(String debtorAccountNumber) {
		this.debtorAccountNumber = debtorAccountNumber;
	}
	public String getDebtorAccountType() {
		return debtorAccountType;
	}
	public void setDebtorAccountType(String debtorAccountType) {
		this.debtorAccountType = debtorAccountType;
	}
	public String getDebtorId() {
		return debtorId;
	}
	public void setDebtorId(String debtorId) {
		this.debtorId = debtorId;
	}
	public String getDebtorType() {
		return debtorType;
	}
	public void setDebtorType(String debtorType) {
		this.debtorType = debtorType;
	}
	public String getCreditorAccountNumber() {
		return creditorAccountNumber;
	}
	public void setCreditorAccountNumber(String creditorAccountNumber) {
		this.creditorAccountNumber = creditorAccountNumber;
	}
	public String getCreditorAccountType() {
		return creditorAccountType;
	}
	public void setCreditorAccountType(String creditorAccountType) {
		this.creditorAccountType = creditorAccountType;
	}
	public String getCreditorId() {
		return creditorId;
	}
	public void setCreditorId(String creditorId) {
		this.creditorId = creditorId;
	}
	public String getCreditorType() {
		return creditorType;
	}
	public void setCreditorType(String creditorType) {
		this.creditorType = creditorType;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCrdtTrnRequestBizMsgIdr() {
		return crdtTrnRequestBizMsgIdr;
	}
	public void setCrdtTrnRequestBizMsgIdr(String crdtTrnRequestBizMsgIdr) {
		this.crdtTrnRequestBizMsgIdr = crdtTrnRequestBizMsgIdr;
	}
	public String getEndToEndId() {
		return endToEndId;
	}
	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}
	public String getCrdtTrnResponseBizMsgIdr() {
		return crdtTrnResponseBizMsgIdr;
	}
	public void setCrdtTrnResponseBizMsgIdr(String crdtTrnResponseBizMsgIdr) {
		this.crdtTrnResponseBizMsgIdr = crdtTrnResponseBizMsgIdr;
	}
	public String getSettlementConfBizMsgIdr() {
		return settlementConfBizMsgIdr;
	}
	public void setSettlementConfBizMsgIdr(String settlementConfBizMsgIdr) {
		this.settlementConfBizMsgIdr = settlementConfBizMsgIdr;
	}
	public LocalDateTime getCihubRequestDT() {
		return cihubRequestDT;
	}
	public void setCihubRequestDT(LocalDateTime cihubRequestDT) {
		this.cihubRequestDT = cihubRequestDT;
	}
	public String getCpyDplct() {
		return cpyDplct;
	}
	public void setCpyDplct(String cpyDplct) {
		this.cpyDplct = cpyDplct;
	}
	public Long getCihubElapsedTime() {
		return cihubElapsedTime;
	}
	public void setCihubElapsedTime(Long cihubElapsedTime) {
		this.cihubElapsedTime = cihubElapsedTime;
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
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Integer getPsCounter() {
		return psCounter;
	}
	public void setPsCounter(Integer psCounter) {
		this.psCounter = psCounter;
	}
	public String getReversal() {
		return reversal;
	}
	public void setReversal(String reversal) {
		this.reversal = reversal;
	}
	public String getCbStatus() {
		return cbStatus;
	}
	public void setCbStatus(String cbStatus) {
		this.cbStatus = cbStatus;
	}
	public String getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public LocalDateTime getCreateDt() {
		return createDt;
	}
	public void setCreateDt(LocalDateTime createDt) {
		this.createDt = createDt;
	}
	public LocalDateTime getLastUpdateDt() {
		return lastUpdateDt;
	}
	public void setLastUpdateDt(LocalDateTime lastUpdateDt) {
		this.lastUpdateDt = lastUpdateDt;
	}
	


	
}
