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
@Table(name="KC_COREBANK_TRANSACTION")
public class CorebankTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
	@SequenceGenerator(name="seq_generator", sequenceName = "table_seq_generator", allocationSize=1)
	private Long id;

	@Column(length=50)
	private String komiTrnsId;
	
	@Column(length=8)
	private String trnsDate;
	
	@Column(length=50)
	private String komiNoref;     // jadi id ke core
	@Column(length=50)
	private String dateTime;  // untuk id ke core
	
	@Column(length=20)
	private String transactionType;   //  DebitAccount, DebitReversal, CreditAccount, Settlement, AccountEnquiry, AccountCustInfo
	
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;
	private BigDecimal feeAmount;
	
	@Column(length=50)
	private String cstmAccountNo;
	@Column(length=10)
	private String cstmAccountType;
	@Column(length=140)
	private String cstmAccountName;

	private String orgnlChnlNoref;
	@Column(length=50)
	private String orgnlDateTime;

	@Column(length=20)
	private String response;
	@Column(length=20)
	private String reason;

	private Integer retryCounter;
	
	private LocalDateTime updateTime;
	
	@Column(length=2000)
	private String fullTextRequest;

//	@Column(length=50)
//	private String settlBizmsgidr;
	
	
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

	public String getTrnsDate() {
		return trnsDate;
	}

	public void setTrnsDate(String trnsDate) {
		this.trnsDate = trnsDate;
	}

	public String getKomiNoref() {
		return komiNoref;
	}

	public void setKomiNoref(String komiNoref) {
		this.komiNoref = komiNoref;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getCstmAccountNo() {
		return cstmAccountNo;
	}

	public void setCstmAccountNo(String cstmAccountNo) {
		this.cstmAccountNo = cstmAccountNo;
	}

	public String getCstmAccountType() {
		return cstmAccountType;
	}

	public void setCstmAccountType(String cstmAccountType) {
		this.cstmAccountType = cstmAccountType;
	}

	public String getCstmAccountName() {
		return cstmAccountName;
	}

	public void setCstmAccountName(String cstmAccountName) {
		this.cstmAccountName = cstmAccountName;
	}

	public String getOrgnlChnlNoref() {
		return orgnlChnlNoref;
	}

	public void setOrgnlChnlNoref(String orgnlChnlNoref) {
		this.orgnlChnlNoref = orgnlChnlNoref;
	}

	public String getOrgnlDateTime() {
		return orgnlDateTime;
	}

	public void setOrgnlDateTime(String orgnlDateTime) {
		this.orgnlDateTime = orgnlDateTime;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getFullTextRequest() {
		return fullTextRequest;
	}

	public void setFullTextRequest(String fullTextRequest) {
		this.fullTextRequest = fullTextRequest;
	}

//	public String getSettlBizmsgidr() {
//		return settlBizmsgidr;
//	}
//
//	public void setSettlBizmsgidr(String settlBizmsgidr) {
//		this.settlBizmsgidr = settlBizmsgidr;
//	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getRetryCounter() {
		return retryCounter;
	}

	public void setRetryCounter(Integer retryCounter) {
		this.retryCounter = retryCounter;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}



	

	
}
