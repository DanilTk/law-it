package pl.lawit.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.domain.model.RegisteredFile;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface RegisteredFileRepositoryJpa extends JpaRepository<RegisteredFileEntity, UUID> {

	default RegisteredFileEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, RegisteredFile.class));
	}

}
