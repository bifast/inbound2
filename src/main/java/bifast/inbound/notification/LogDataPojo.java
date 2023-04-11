package bifast.inbound.notification;

public class LogDataPojo {

	private String komi_unique_id;
	private String komi_trx_no;
	private String channel_type;
	private String trx_type;
	private String trx_initiation_date;
	private String trx_duration;
	private String error_msg;
	private String sender_bank;
	
	private String recipient_bank;    // CT, AE
	private String bifast_trx_no;   // CT, AE, PRX
	private String recipient_account_no;  // CT, AE
	private String recipient_account_name;  // CT
	private String sender_account_no;  // CT, AE, PRX
	private String sender_account_name;  // CT, PRX
	private String proxy_type;  // CT, AE, PRX
	private String proxy_alias; // CT, AE, PRX
	private String proxyFlag;   // CT, AE : Y/T
	
	private String charge_type;  // CT
	private String trx_amount;   // CT, AE
	private String fee_amount;   // CT
	
	private String status_code;
	private String response_code;
	private String reason_code;
	private String reason_message;
	
	private String proxy_regn_opr; // PRX
	private String scnd_id_value; // PRX
	private String scnd_id_type; // PRX
	
	public String getKomi_unique_id() {
		return komi_unique_id;
	}
	public void setKomi_unique_id(String komi_unique_id) {
		this.komi_unique_id = komi_unique_id;
	}
	public String getKomi_trx_no() {
		return komi_trx_no;
	}
	public void setKomi_trx_no(String komi_trx_no) {
		this.komi_trx_no = komi_trx_no;
	}
	public String getChannel_type() {
		return channel_type;
	}
	public void setChannel_type(String channel_type) {
		this.channel_type = channel_type;
	}
	public String getFee_amount() {
		return fee_amount;
	}
	public void setFee_amount(String fee_amount) {
		this.fee_amount = fee_amount;
	}
	public String getSender_account_no() {
		return sender_account_no;
	}
	public void setSender_account_no(String sender_account_no) {
		this.sender_account_no = sender_account_no;
	}
	public String getSender_account_name() {
		return sender_account_name;
	}
	public void setSender_account_name(String sender_account_name) {
		this.sender_account_name = sender_account_name;
	}
	public String getTrx_type() {
		return trx_type;
	}
	public void setTrx_type(String trx_type) {
		this.trx_type = trx_type;
	}
	public String getTrx_initiation_date() {
		return trx_initiation_date;
	}
	public void setTrx_initiation_date(String trx_initiation_date) {
		this.trx_initiation_date = trx_initiation_date;
	}
	public String getTrx_duration() {
		return trx_duration;
	}
	public void setTrx_duration(String trx_duration) {
		this.trx_duration = trx_duration;
	}
//	public String getCall_status() {
//		return call_status;
//	}
//	public void setCall_status(String call_status) {
//		this.call_status = call_status;
//	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	public String getSender_bank() {
		return sender_bank;
	}
	public void setSender_bank(String sender_bank) {
		this.sender_bank = sender_bank;
	}
	public String getRecipient_bank() {
		return recipient_bank;
	}
	public void setRecipient_bank(String recipient_bank) {
		this.recipient_bank = recipient_bank;
	}
	public String getBifast_trx_no() {
		return bifast_trx_no;
	}
	public void setBifast_trx_no(String bifast_trx_no) {
		this.bifast_trx_no = bifast_trx_no;
	}
	public String getRecipient_account_no() {
		return recipient_account_no;
	}
	public void setRecipient_account_no(String recipient_account_no) {
		this.recipient_account_no = recipient_account_no;
	}
	public String getRecipient_account_name() {
		return recipient_account_name;
	}
	public void setRecipient_account_name(String recipient_account_name) {
		this.recipient_account_name = recipient_account_name;
	}
	public String getProxy_type() {
		return proxy_type;
	}
	public void setProxy_type(String proxy_type) {
		this.proxy_type = proxy_type;
	}
	public String getProxy_alias() {
		return proxy_alias;
	}
	public void setProxy_alias(String proxy_alias) {
		this.proxy_alias = proxy_alias;
	}
	public String getProxyFlag() {
		return proxyFlag;
	}
	public void setProxyFlag(String proxyFlag) {
		this.proxyFlag = proxyFlag;
	}
	public String getCharge_type() {
		return charge_type;
	}
	public void setCharge_type(String charge_type) {
		this.charge_type = charge_type;
	}
	public String getTrx_amount() {
		return trx_amount;
	}
	public void setTrx_amount(String trx_amount) {
		this.trx_amount = trx_amount;
	}
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getReason_code() {
		return reason_code;
	}
	public void setReason_code(String reason_code) {
		this.reason_code = reason_code;
	}
	public String getResponse_code() {
		return response_code;
	}
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}
	public String getReason_message() {
		return reason_message;
	}
	public void setReason_message(String reason_message) {
		this.reason_message = reason_message;
	}
	public String getProxy_regn_opr() {
		return proxy_regn_opr;
	}
	public void setProxy_regn_opr(String proxy_regn_opr) {
		this.proxy_regn_opr = proxy_regn_opr;
	}
	public String getScnd_id_value() {
		return scnd_id_value;
	}
	public void setScnd_id_value(String scnd_id_value) {
		this.scnd_id_value = scnd_id_value;
	}
	public String getScnd_id_type() {
		return scnd_id_type;
	}
	public void setScnd_id_type(String scnd_id_type) {
		this.scnd_id_type = scnd_id_type;
	}
    	

	
}
