package bifast.inbound.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bifast.inbound.model.FaultClass;

@Repository
public interface FaultClassRepository extends JpaRepository<FaultClass, Long> {

	Optional<FaultClass> findByExceptionClass (String exceptionClass);
	
}
