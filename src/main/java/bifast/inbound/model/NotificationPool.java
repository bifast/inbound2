package bifast.inbound.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="KC_NOTIFICATION_POOL")
public class NotificationPool {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
	@SequenceGenerator(name="seq_generator", sequenceName = "table_seq_generator", allocationSize=1)
	private Long id;

	private String eventCtgr;    // business/system event
	private String notifLevel;  // ALERT-ERROR-INFO
	private String urgency;     // HIGH-MEDIUM-LOW
	private String eventGrp;   // receive Credit / CT Success-pending-reject RJCT-CIHUB/TIMEOUT-CIHUB/ERROR-CIHUB
	private String refId;      // channelRefId-IntrnRefId-bizmsgidr
	private String messageDesc;
	private String destination;
	private String distributionChannel;    //
	private String customerId;
	private String customerAccount;
	private String customerName;
	private String emailAddr;
	private String phoneNo;
	
	private LocalDateTime creDt;
	private String ackStatus;
	private LocalDateTime ackDt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEventCtgr() {
		return eventCtgr;
	}
	public void setEventCtgr(String eventCtgr) {
		this.eventCtgr = eventCtgr;
	}
	public String getNotifLevel() {
		return notifLevel;
	}
	public void setNotifLevel(String notifLevel) {
		this.notifLevel = notifLevel;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public String getEventGrp() {
		return eventGrp;
	}
	public void setEventGrp(String eventGrp) {
		this.eventGrp = eventGrp;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getMessageDesc() {
		return messageDesc;
	}
	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDistributionChannel() {
		return distributionChannel;
	}
	public void setDistributionChannel(String distributionChannel) {
		this.distributionChannel = distributionChannel;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerAccount() {
		return customerAccount;
	}
	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public LocalDateTime getCreDt() {
		return creDt;
	}
	public void setCreDt(LocalDateTime creDt) {
		this.creDt = creDt;
	}
	public String getAckStatus() {
		return ackStatus;
	}
	public void setAckStatus(String ackStatus) {
		this.ackStatus = ackStatus;
	}
	public LocalDateTime getAckDt() {
		return ackDt;
	}
	public void setAckDt(LocalDateTime ackDt) {
		this.ackDt = ackDt;
	}

	
	
}
