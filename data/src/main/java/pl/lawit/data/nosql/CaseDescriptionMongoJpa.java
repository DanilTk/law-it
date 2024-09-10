package pl.lawit.data.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.lawit.data.document.CaseDescriptionDocument;
import pl.lawit.domain.model.Case;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface CaseDescriptionMongoJpa extends MongoRepository<CaseDescriptionDocument, UUID> {

	default CaseDescriptionDocument getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, Case.class));
	}

}
