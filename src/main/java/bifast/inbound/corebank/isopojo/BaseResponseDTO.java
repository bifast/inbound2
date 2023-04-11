package bifast.inbound.corebank.isopojo;

public class BaseResponseDTO extends BaseRequestDTO{

    private String status;
    
    private String reason;

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

    
}
