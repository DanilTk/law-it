package pl.lawit.data.jpa;

import io.vavr.collection.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.lawit.data.entity.LawyerEntity;
import pl.lawit.domain.model.Lawyer;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface LawyerRepositoryJpa extends JpaRepository<LawyerEntity, UUID>,
	JpaSpecificationExecutor<LawyerEntity> {

	default LawyerEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, Lawyer.class));
	}

	LawyerEntity findByApplicationUserUuid(UUID uuid);

	List<LawyerEntity> findAllByCompanyUuid(UUID uuid);

	boolean existsByCompanyUuid(UUID uuid);

	boolean existsByPesel(String pesel);

	boolean existsByApplicationUserEmail(String email);

}
