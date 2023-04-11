package bifast.inbound.paymentstatus;

import java.time.LocalDateTime;

public class CTQryDTO {

	public Long id;
	public String reqBizmsgid;
	public String KomiTrnsId;
	public String endToEndId;
	public LocalDateTime lastUpdateDt;
	public String recipientBank;
	public String ctFullText;
	
	public int psCounter;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReqBizmsgid() {
		return reqBizmsgid;
	}
	public void setReqBizmsgid(String reqBizmsgid) {
		this.reqBizmsgid = reqBizmsgid;
	}
	public String getKomiTrnsId() {
		return KomiTrnsId;
	}
	public void setKomiTrnsId(String komiTrnsId) {
		KomiTrnsId = komiTrnsId;
	}
	public String getEndToEndId() {
		return endToEndId;
	}
	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}
	public String getRecipientBank() {
		return recipientBank;
	}
	public void setRecipientBank(String recipientBank) {
		this.recipientBank = recipientBank;
	}
	public String getCtFullText() {
		return ctFullText;
	}
	public void setCtFullText(String ctFullText) {
		this.ctFullText = ctFullText;
	}
	public int getPsCounter() {
		return psCounter;
	}
	public void setPsCounter(int psCounter) {
		this.psCounter = psCounter;
	}
	public LocalDateTime getLastUpdateDt() {
		return lastUpdateDt;
	}
	public void setLastUpdateDt(LocalDateTime lastUpdateDt) {
		this.lastUpdateDt = lastUpdateDt;
	}

	
}
