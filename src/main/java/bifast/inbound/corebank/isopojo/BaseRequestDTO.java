package bifast.inbound.corebank.isopojo;

public class BaseRequestDTO {
    private String transactionId;
    
    private String noRef;
    
    private String merchantType;
    
    private String terminalId;
    
    private String dateTime;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getNoRef() {
		return noRef;
	}

	public void setNoRef(String noRef) {
		this.noRef = noRef;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

    
}
