package bifast.inbound.pojo.flat;

import java.math.BigDecimal;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("Pacs008")
public class FlatPacs008Pojo extends FlatMessageBase {

	private String msgId;
	private String creDtTm;
	private String SettlementMtd;
	private String endToEndId;
	private String transactionId;
	private String paymentChannel;
	private String categoryPurpose;
	private BigDecimal amount;
	private String currency;
	private String chargeBearer;
	
	private String debtorName;
	private String debtorId;
//	private String debtorOrgId;
//	private String debtorPrvId;
	
	private String debtorAccountNo;
	private String debtorAccountType;
	private String debtorAgentId;
	private String creditorAgentId;
	private String creditorName;
	private String creditorId;
//	private String creditorOrgId;
	
	private String creditorAccountNo;
	private String creditorAccountType;
	
	private String creditorAccountProxyId;
	private String creditorAccountProxyType;
	private String paymentInfo;
	
	private String debtorType;
	private String debtorResidentialStatus;
	private String debtorTownName;
	
	private String creditorType;
	private String creditorResidentialStatus;
	private String creditorTownName;
	
	private String orgnlEndToEndId;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getCreDtTm() {
		return creDtTm;
	}
	public void setCreDtTm(String creDtTm) {
		this.creDtTm = creDtTm;
	}
	public String getSettlementMtd() {
		return SettlementMtd;
	}
	public void setSettlementMtd(String settlementMtd) {
		SettlementMtd = settlementMtd;
	}
	public String getEndToEndId() {
		return endToEndId;
	}
	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public String getCategoryPurpose() {
		return categoryPurpose;
	}
	public void setCategoryPurpose(String categoryPurpose) {
		this.categoryPurpose = categoryPurpose;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getChargeBearer() {
		return chargeBearer;
	}
	public void setChargeBearer(String chargeBearer) {
		this.chargeBearer = chargeBearer;
	}
	public String getDebtorName() {
		return Optional.ofNullable(debtorName).orElse("") ;
	}
	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}
	public String getDebtorId() {
		return Optional.ofNullable(debtorId).orElse("") ;
	}
	public void setDebtorId(String debtorId) {
		this.debtorId = debtorId;
	}
//	public String getDebtorPrvId() {
//		return debtorPrvId;
//	}
//	public void setDebtorPrvId(String debtorPrvId) {
//		this.debtorPrvId = debtorPrvId;
//	}
	public String getDebtorAccountNo() {
		return debtorAccountNo;
	}
	public void setDebtorAccountNo(String debtorAccountNo) {
		this.debtorAccountNo = debtorAccountNo;
	}
	public String getDebtorAccountType() {
		return debtorAccountType;
	}
	public void setDebtorAccountType(String debtorAccountType) {
		this.debtorAccountType = debtorAccountType;
	}
	public String getDebtorAgentId() {
		return debtorAgentId;
	}
	public void setDebtorAgentId(String debtorAgentId) {
		this.debtorAgentId = debtorAgentId;
	}
	public String getCreditorAgentId() {
		return creditorAgentId;
	}
	public void setCreditorAgentId(String creditorAgentId) {
		this.creditorAgentId = creditorAgentId;
	}
	public String getCreditorName() {
		return creditorName;
	}
	public void setCreditorName(String creditorName) {
		this.creditorName = creditorName;
	}
	public String getCreditorId() {
		return creditorId;
	}
	public void setCreditorId(String creditorId) {
		this.creditorId = creditorId;
	}
//	public String getCreditorOrgId() {
//		return creditorOrgId;
//	}
//	public void setCreditorOrgId(String creditorOrgId) {
//		this.creditorOrgId = creditorOrgId;
//	}
	public String getCreditorAccountNo() {
		return creditorAccountNo;
	}
	public void setCreditorAccountNo(String creditorAccountNo) {
		this.creditorAccountNo = creditorAccountNo;
	}
	public String getCreditorAccountType() {
		return creditorAccountType;
	}
	public void setCreditorAccountType(String creditorAccountType) {
		this.creditorAccountType = creditorAccountType;
	}
	public String getCreditorAccountProxyId() {
		return creditorAccountProxyId;
	}
	public void setCreditorAccountProxyId(String creditorAccountProxyId) {
		this.creditorAccountProxyId = creditorAccountProxyId;
	}
	public String getCreditorAccountProxyType() {
		return creditorAccountProxyType;
	}
	public void setCreditorAccountProxyType(String creditorAccountProxyType) {
		this.creditorAccountProxyType = creditorAccountProxyType;
	}
	public String getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	public String getDebtorType() {
		return debtorType;
	}
	public void setDebtorType(String debtorType) {
		this.debtorType = debtorType;
	}
	public String getDebtorResidentialStatus() {
		return debtorResidentialStatus;
	}
	public void setDebtorResidentialStatus(String debtorResidentialStatus) {
		this.debtorResidentialStatus = debtorResidentialStatus;
	}
	public String getDebtorTownName() {
		return debtorTownName;
	}
	public void setDebtorTownName(String debtorTownName) {
		this.debtorTownName = debtorTownName;
	}
	public String getCreditorType() {
		return creditorType;
	}
	public void setCreditorType(String creditorType) {
		this.creditorType = creditorType;
	}
	public String getCreditorResidentialStatus() {
		return creditorResidentialStatus;
	}
	public void setCreditorResidentialStatus(String creditorResidentialStatus) {
		this.creditorResidentialStatus = creditorResidentialStatus;
	}
	public String getCreditorTownName() {
		return creditorTownName;
	}
	public void setCreditorTownName(String creditorTownName) {
		this.creditorTownName = creditorTownName;
	}
	public String getOrgnlEndToEndId() {
		return orgnlEndToEndId;
	}
	public void setOrgnlEndToEndId(String orgnlEndToEndId) {
		this.orgnlEndToEndId = orgnlEndToEndId;
	}
	
	
}
