package bifast.inbound.notification;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("apimessage")
public class PortalApiPojo {

	private String codelog;
	
	private LogDataPojo data;

	public String getCodelog() {
		return codelog;
	}

	public void setCodelog(String codelog) {
		this.codelog = codelog;
	}

	public LogDataPojo getData() {
		return data;
	}

	public void setData(LogDataPojo data) {
		this.data = data;
	}
	
}
