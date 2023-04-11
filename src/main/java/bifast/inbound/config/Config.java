package bifast.inbound.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration  // so  Sprint creates bean in application context
@ConfigurationProperties(prefix = "komi")
public class Config {

	private String bankcode;
	private Map<String,String> ciConnector; 
	private Map<String,String> corebank;
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	public Map<String, String> getCiConnector() {
		return ciConnector;
	}
	public void setCiConnector(Map<String, String> ciConnector) {
		this.ciConnector = ciConnector;
	}
	public Map<String, String> getCorebank() {
		return corebank;
	}
	public void setCorebank(Map<String, String> corebank) {
		this.corebank = corebank;
	}
}
