package pl.lawit.data.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.lawit.data.entity.CaseEntity;
import pl.lawit.domain.model.Case;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface CaseRepositoryJpa extends JpaRepository<CaseEntity, UUID>,
	JpaSpecificationExecutor<CaseEntity> {

	default CaseEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, Case.class));
	}

	Page<CaseEntity> findAllByCompanyUuid(UUID uuid, Pageable pageable);

	Page<CaseEntity> findAllByLawyerUuid(UUID uuid, Pageable pageable);

	Page<CaseEntity> findAllByCreatedByUuid(UUID uuid, Pageable pageable);

}
