package pl.lawit.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.lawit.data.entity.DocumentTemplateEntity;
import pl.lawit.domain.model.DocumentTemplate;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface DocumentTemplateRepositoryJpa extends JpaRepository<DocumentTemplateEntity, UUID>,
	JpaSpecificationExecutor<DocumentTemplateEntity> {

	default DocumentTemplateEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, DocumentTemplate.class));
	}

}
