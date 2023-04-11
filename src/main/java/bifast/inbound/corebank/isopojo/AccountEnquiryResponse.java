package bifast.inbound.corebank.isopojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AccountEnquiryResponse extends BaseRequestDTO{

    private String status;
    
    private String reason;

    private String accountNumber;
    @JsonInclude(Include.NON_NULL)
    private String accountType;
    
    @JsonInclude(Include.NON_NULL)
    private String creditorName;
    
    @JsonInclude(Include.NON_NULL)
    private String creditorId;
    
    @JsonInclude(Include.NON_NULL)
    private String creditorType;
    
    @JsonInclude(Include.NON_NULL)
    private String residentStatus;
    
    @JsonInclude(Include.NON_NULL)
    private String townName;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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

	public String getCreditorType() {
		return creditorType;
	}

	public void setCreditorType(String creditorType) {
		this.creditorType = creditorType;
	}

	public String getResidentStatus() {
		return residentStatus;
	}

	public void setResidentStatus(String residentStatus) {
		this.residentStatus = residentStatus;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

    
}
