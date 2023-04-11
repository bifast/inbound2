package bifast.inbound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bifast.inbound.model.Settlement;


@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {

//	public List<Settlement> findByOrgnlCTBizMsgId (String reqBizMsgId);
	public List<Settlement> findByOrgnlEndToEndId (String orgnlEndToEndId);
	
}
