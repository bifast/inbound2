package bifast.inbound.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bifast.inbound.model.StatusReason;

@Repository
public interface StatusReasonRepository extends JpaRepository<StatusReason, String> {

}
