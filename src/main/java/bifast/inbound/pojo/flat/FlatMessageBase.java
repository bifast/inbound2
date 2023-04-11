package bifast.inbound.pojo.flat;

public class FlatMessageBase {

	private String frBic;
	private String toBic;
	private String bizMsgIdr;
	private String msgDefIdr;
	private String bizSvc;
	private String creDt;
	private String CpyDplct;
	private Boolean pssblDplct;
	
	public String getFrBic() {
		return frBic;
	}
	public void setFrBic(String frBic) {
		this.frBic = frBic;
	}
	public String getToBic() {
		return toBic;
	}
	public void setToBic(String toBic) {
		this.toBic = toBic;
	}
	public String getBizMsgIdr() {
		return bizMsgIdr;
	}
	public void setBizMsgIdr(String bizMsgIdr) {
		this.bizMsgIdr = bizMsgIdr;
	}
	public String getMsgDefIdr() {
		return msgDefIdr;
	}
	public void setMsgDefIdr(String msgDefIdr) {
		this.msgDefIdr = msgDefIdr;
	}
	public String getBizSvc() {
		return bizSvc;
	}
	public void setBizSvc(String bizSvc) {
		this.bizSvc = bizSvc;
	}
	public String getCreDt() {
		return creDt;
	}
	public void setCreDt(String creDt) {
		this.creDt = creDt;
	}
	public String getCpyDplct() {
		return CpyDplct;
	}
	public void setCpyDplct(String cpyDplct) {
		CpyDplct = cpyDplct;
	}
	public Boolean getPssblDplct() {
		return pssblDplct;
	}
	public void setPssblDplct(Boolean pssblDplct) {
		this.pssblDplct = pssblDplct;
	}
	
	
	
}
