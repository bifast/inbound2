package bifast.inbound.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bifast.inbound.model.CreditTransfer;


@Repository
public interface CreditTransferRepository extends JpaRepository<CreditTransfer, Long> {

	public List<CreditTransfer> findAllByCrdtTrnRequestBizMsgIdr (String msgId);
//	public List<CreditTransfer> findAllByCrdtTrnRequestBizMsgIdrAndResponseCode (String msgId, String responseCode);
	public List<CreditTransfer> findAllByEndToEndId (String endToEndId);
	
	String qry = "select ct from CreditTransfer ct "
			+ "where ct.endToEndId = :end2endId";
//			+ "join CorebankTransaction cb "
//			+ "on cb.komiTrnsId = ct.komiTrnsId "
//			+ "and ct.endToEndId = :end2endid";
	public Optional<CreditTransfer> getSuccessByEndToEndId (@Param("end2endid") String end2endId);

//	public Optional<CreditTransfer> findByCrdtTrnRequestBizMsgIdr (String msgId);

}
