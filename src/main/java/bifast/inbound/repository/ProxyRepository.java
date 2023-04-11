package bifast.inbound.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bifast.inbound.model.Proxy;


@Repository
public interface ProxyRepository extends JpaRepository<Proxy, Long> {

	@Query("SELECT p FROM Proxy p WHERE " +
            "p.proxyType = :prxType AND " +
            "p.proxyValue = :prxValue AND " +
			"p.proxyStatus <> 'ICTV' ")
	public Optional<Proxy> getByProxyTypeAndProxyValue (
			@Param("prxType") String prxType, 
			@Param("prxValue") String prxValue);
	
}
