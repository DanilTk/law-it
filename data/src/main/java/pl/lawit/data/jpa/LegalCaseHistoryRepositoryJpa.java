package pl.lawit.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.lawit.data.entity.LegalCaseHistoryEntity;

import java.util.UUID;

public interface LegalCaseHistoryRepositoryJpa extends JpaRepository<LegalCaseHistoryEntity, UUID>,
	JpaSpecificationExecutor<LegalCaseHistoryEntity> {

}
