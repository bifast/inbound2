package bifast.inbound.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="KC_FAULT_CLASS")
public class FaultClass {

	@Id
	private Long id;
	private String exceptionClass;
	private String reason;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getExceptionClass() {
		return exceptionClass;
	}
	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
