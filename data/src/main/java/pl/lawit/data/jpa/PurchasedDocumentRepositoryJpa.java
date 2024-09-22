package pl.lawit.data.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.lawit.data.entity.PurchasedDocumentEntity;
import pl.lawit.domain.model.PurchasedDocument;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface PurchasedDocumentRepositoryJpa extends JpaRepository<PurchasedDocumentEntity, UUID>,
	JpaSpecificationExecutor<PurchasedDocumentEntity> {

	default PurchasedDocumentEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, PurchasedDocument.class));
	}

	Page<PurchasedDocumentEntity> findAllByCreatedByUuid(UUID createdByUuid, Pageable pageable);

}
