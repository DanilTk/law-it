package pl.lawit.data.jpa;

import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface ApplicationUserRepositoryJpa extends JpaRepository<ApplicationUserEntity, UUID>,
	JpaSpecificationExecutor<ApplicationUserEntity> {

	default ApplicationUserEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, ApplicationUser.class));
	}

	Option<ApplicationUserEntity> findByIdpSub(String idpSub);

	Option<ApplicationUserEntity> findByEmail(String email);

}
