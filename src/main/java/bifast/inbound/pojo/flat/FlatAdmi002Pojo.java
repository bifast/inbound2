package bifast.inbound.pojo.flat;

public class FlatAdmi002Pojo extends FlatMessageBase {
	
	private String relatedRef;
	private String rejectReason;
	private String rejectDateTime;
	private String errorLocation;
	private String reasonDesc;
	private String additionalData;
	
	public String getRelatedRef() {
		return relatedRef;
	}
	public void setRelatedRef(String relatedRef) {
		this.relatedRef = relatedRef;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public String getRejectDateTime() {
		return rejectDateTime;
	}
	public void setRejectDateTime(String rejectDateTime) {
		this.rejectDateTime = rejectDateTime;
	}
	public String getErrorLocation() {
		return errorLocation;
	}
	public void setErrorLocation(String errorLocation) {
		this.errorLocation = errorLocation;
	}
	public String getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	public String getAdditionalData() {
		return additionalData;
	}
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}
	
	
	
	

}
