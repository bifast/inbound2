package bifast.inbound.isoservice;

import java.math.BigDecimal;

public class Pacs008Seed {

	private String msgId;
	private String trnType;   
	private String bizMsgId; 
	private String endToEndId;
	
	private BigDecimal amount; 
	private String orignBank; 
	private String recptBank; 
	private String channel; 
	private String categoryPurpose;	
	private String dbtrId;   
	private String dbtrName;
	private String dbtrType;  
	private String dbtrAccountNo;  
	private String dbtrAccountType;
	private String dbtrResidentStatus;
	private String dbtrTownName;
	
	private String crdtId;  // CrdtTrn
	private String crdtName;
	private String crdtType; 
	private String crdtAccountNo;   // AcctEnq CrdtTrn
	private String crdtAccountType;  // CrdtTrn
	private String crdtResidentStatus;
	private String crdtTownName;

	private String crdtProxyIdType;
	private String crdtProxyIdValue;

	private String paymentInfo;  // CrdtTrn
	
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Pacs008Seed () {}
	
	public String getTrnType() {
		return trnType;
	}
	public void setTrnType(String trnType) {
		this.trnType = trnType;
	}
	public String getBizMsgId() {
		return bizMsgId;
	}
	public void setBizMsgId(String bizMsgId) {
		this.bizMsgId = bizMsgId;
	}
	public String getEndToEndId() {
		return endToEndId;
	}

	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getOrignBank() {
		return orignBank;
	}
	public void setOrignBank(String orignBank) {
		this.orignBank = orignBank;
	}
	public String getRecptBank() {
		return recptBank;
	}
	public void setRecptBank(String recptBank) {
		this.recptBank = recptBank;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCategoryPurpose() {
		return categoryPurpose;
	}
	public void setCategoryPurpose(String categoryPurpose) {
		this.categoryPurpose = categoryPurpose;
	}
	public String getDbtrId() {
		return dbtrId;
	}
	public void setDbtrId(String dbtrId) {
		this.dbtrId = dbtrId;
	}
	public String getDbtrName() {
		return dbtrName;
	}

	public void setDbtrName(String dbtrName) {
		this.dbtrName = dbtrName;
	}

	public String getDbtrType() {
		return dbtrType;
	}
	public void setDbtrType(String dbtrType) {
		this.dbtrType = dbtrType;
	}
	public String getDbtrAccountNo() {
		return dbtrAccountNo;
	}
	public void setDbtrAccountNo(String dbtrAccountNo) {
		this.dbtrAccountNo = dbtrAccountNo;
	}
	public String getDbtrAccountType() {
		return dbtrAccountType;
	}
	public void setDbtrAccountType(String dbtrAccountType) {
		this.dbtrAccountType = dbtrAccountType;
	}

	public String getCrdtId() {
		return crdtId;
	}

	public void setCrdtId(String crdtId) {
		this.crdtId = crdtId;
	}

	public String getCrdtName() {
		return crdtName;
	}

	public void setCrdtName(String crdtName) {
		this.crdtName = crdtName;
	}

	public String getCrdtType() {
		return crdtType;
	}

	public void setCrdtType(String crdtType) {
		this.crdtType = crdtType;
	}

	public String getCrdtAccountNo() {
		return crdtAccountNo;
	}

	public void setCrdtAccountNo(String crdtAccountNo) {
		this.crdtAccountNo = crdtAccountNo;
	}

	public String getCrdtAccountType() {
		return crdtAccountType;
	}

	public void setCrdtAccountType(String crdtAccountType) {
		this.crdtAccountType = crdtAccountType;
	}

	public String getDbtrResidentStatus() {
		return dbtrResidentStatus;
	}

	public void setDbtrResidentStatus(String dbtrResidentStatus) {
		this.dbtrResidentStatus = dbtrResidentStatus;
	}

	public String getDbtrTownName() {
		return dbtrTownName;
	}

	public void setDbtrTownName(String dbtrTownName) {
		this.dbtrTownName = dbtrTownName;
	}

	public String getCrdtResidentStatus() {
		return crdtResidentStatus;
	}

	public void setCrdtResidentStatus(String crdtResidentStatus) {
		this.crdtResidentStatus = crdtResidentStatus;
	}

	public String getCrdtTownName() {
		return crdtTownName;
	}

	public void setCrdtTownName(String crdtTownName) {
		this.crdtTownName = crdtTownName;
	}

	public String getCrdtProxyIdType() {
		return crdtProxyIdType;
	}

	public void setCrdtProxyIdType(String crdtProxyIdType) {
		this.crdtProxyIdType = crdtProxyIdType;
	}

	public String getCrdtProxyIdValue() {
		return crdtProxyIdValue;
	}

	public void setCrdtProxyIdValue(String crdtProxyIdValue) {
		this.crdtProxyIdValue = crdtProxyIdValue;
	}

	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	

	
}
