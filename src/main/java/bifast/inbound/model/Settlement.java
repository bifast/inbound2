package bifast.inbound.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="KC_SETTLEMENT")
public class Settlement {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
	@SequenceGenerator(name="seq_generator", sequenceName = "table_seq_generator", allocationSize=1)
	private Long id;
	
	@Column(length=20)
	private String dbtrBank;
	@Column(length=20)
	private String crdtBank;
	
	@Column(name="SETTL_BIZMSGID", length=50)
	private String settlBizMsgId;
	@Column(name="ORGNL_CT_BIZMSGID", length=50)
	private String orgnlCTBizMsgId;
	@Column(name="E2E_ID", length=50)
	private String orgnlEndToEndId;

	@Column(length=50)
	private String komiTrnsId;
	
	@Column(length=20)
	private String cbResponse;
	

	@Column(length=35)
	private String crdtAccountNo;
	@Column(length=35)
	private String dbtrAccountNo;
	
	private LocalDateTime receiveDate;
	
	@Column(length=5000)
	private String fullMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDbtrBank() {
		return dbtrBank;
	}

	public void setDbtrBank(String dbtrBank) {
		this.dbtrBank = dbtrBank;
	}

	public String getCrdtBank() {
		return crdtBank;
	}

	public void setCrdtBank(String crdtBank) {
		this.crdtBank = crdtBank;
	}

	public String getSettlBizMsgId() {
		return settlBizMsgId;
	}

	public void setSettlBizMsgId(String settlBizMsgId) {
		this.settlBizMsgId = settlBizMsgId;
	}

	public String getOrgnlCTBizMsgId() {
		return orgnlCTBizMsgId;
	}

	public void setOrgnlCTBizMsgId(String orgnlCTBizMsgId) {
		this.orgnlCTBizMsgId = orgnlCTBizMsgId;
	}

	public String getOrgnlEndToEndId() {
		return orgnlEndToEndId;
	}

	public void setOrgnlEndToEndId(String orgnlEndToEndId) {
		this.orgnlEndToEndId = orgnlEndToEndId;
	}

	public String getCrdtAccountNo() {
		return crdtAccountNo;
	}

	public void setCrdtAccountNo(String crdtAccountNo) {
		this.crdtAccountNo = crdtAccountNo;
	}

	public String getDbtrAccountNo() {
		return dbtrAccountNo;
	}

	public void setDbtrAccountNo(String dbtrAccountNo) {
		this.dbtrAccountNo = dbtrAccountNo;
	}

	public LocalDateTime getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(LocalDateTime receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getKomiTrnsId() {
		return komiTrnsId;
	}

	public void setKomiTrnsId(String komiTrnsId) {
		this.komiTrnsId = komiTrnsId;
	}

	public String getCbResponse() {
		return cbResponse;
	}

	public void setCbResponse(String cbResponse) {
		this.cbResponse = cbResponse;
	}

	public String getFullMessage() {
		return fullMessage;
	}

	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}

	
}
