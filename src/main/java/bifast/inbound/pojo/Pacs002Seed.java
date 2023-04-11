package bifast.inbound.pojo;

public class Pacs002Seed {

	private String msgId;
	private String creditorName;   // CST:resp   ACCENQ:resp
	private String creditorType;   // CST:resp
	private String creditorId;     // CST:resp
	private String creditorAccountNo;  // ACCENQ:resp
	private String creditorAccountIdType;    // ACCENQ:resp
	private String creditorResidentialStatus;  // CST:resp   ACENQ:resp
	private String creditorTown;  // CST:resp   ACCENQ:rep
	private String status;  // FI:resp  CST:resp  ACCENQ:resp
	private String reason;     // FI:resp  CST:resp  ACCENQ:resp
	private String additionalInfo;  //    CST:resp   
	
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getCreditorName() {
		return creditorName;
	}
	public void setCreditorName(String creditorName) {
		this.creditorName = creditorName;
	}
	public String getCreditorType() {
		return creditorType;
	}
	public void setCreditorType(String creditorType) {
		this.creditorType = creditorType;
	}
	public String getCreditorAccountNo() {
		return creditorAccountNo;
	}
	public void setCreditorAccountNo(String creditorAccountNo) {
		this.creditorAccountNo = creditorAccountNo;
	}
	public String getCreditorId() {
		return creditorId;
	}
	public void setCreditorId(String creditorId) {
		this.creditorId = creditorId;
	}
	public String getCreditorAccountIdType() {
		return creditorAccountIdType;
	}
	public void setCreditorAccountIdType(String creditorAccountIdType) {
		this.creditorAccountIdType = creditorAccountIdType;
	}
	public String getCreditorResidentialStatus() {
		return creditorResidentialStatus;
	}
	public void setCreditorResidentialStatus(String creditorResidentialStatus) {
		this.creditorResidentialStatus = creditorResidentialStatus;
	}
	public String getCreditorTown() {
		return creditorTown;
	}
	public void setCreditorTown(String creditorTown) {
		this.creditorTown = creditorTown;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	

	
}
