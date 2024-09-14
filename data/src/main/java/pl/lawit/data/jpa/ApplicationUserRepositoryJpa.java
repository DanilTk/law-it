package pl.lawit.data.jpa;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface ApplicationUserRepositoryJpa extends JpaRepository<ApplicationUserEntity, UUID>,
	JpaSpecificationExecutor<ApplicationUserEntity> {

	default ApplicationUserEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, ApplicationUser.class));
	}

	Option<ApplicationUserEntity> findByIdpUid(String idpUid);

	Option<ApplicationUserEntity> findByEmail(String email);

	@Modifying
	@Query("UPDATE ApplicationUserEntity entity SET entity.isIdpUser = false, entity.lastSyncAt = current_timestamp"
		+ " WHERE entity.idpUid IN :collection")
	void disableUsers(List<String> collection);

}
