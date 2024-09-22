package pl.lawit.data.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.lawit.data.entity.LegalCaseEntity;
import pl.lawit.domain.model.LegalCase;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface LegalCaseRepositoryJpa extends JpaRepository<LegalCaseEntity, UUID>,
	JpaSpecificationExecutor<LegalCaseEntity> {

	default LegalCaseEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, LegalCase.class));
	}

	Page<LegalCaseEntity> findAllByCompanyUuid(UUID uuid, Pageable pageable);

	Page<LegalCaseEntity> findAllByLawyer_ApplicationUserUuid(UUID uuid, Pageable pageable);

	Page<LegalCaseEntity> findAllByCreatedByUuid(UUID uuid, Pageable pageable);

}
