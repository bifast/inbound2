package bifast.inbound.corebank.isopojo;

public class SettlementRequest extends BaseRequestDTO{

    private String status;
    
    private String reason;
    
    private String originalNoRef;

    private String additionalInfo;

    private String bizMsgId;

    private String msgId;

    private String counterParty;


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

	public String getOriginalNoRef() {
		return originalNoRef;
	}

	public void setOriginalNoRef(String originalNoRef) {
		this.originalNoRef = originalNoRef;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getBizMsgId() {
		return bizMsgId;
	}

	public void setBizMsgId(String bizMsgId) {
		this.bizMsgId = bizMsgId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getCounterParty() {
		return counterParty;
	}

	public void setCounterParty(String counterParty) {
		this.counterParty = counterParty;
	}

    
}
