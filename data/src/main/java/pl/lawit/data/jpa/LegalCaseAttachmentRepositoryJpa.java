package pl.lawit.data.jpa;

import io.vavr.collection.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lawit.data.entity.CaseAttachmentEntity;

import java.util.UUID;

public interface LegalCaseAttachmentRepositoryJpa extends JpaRepository<CaseAttachmentEntity, UUID> {

	@Query("SELECT a.file.uuid FROM CaseAttachmentEntity a WHERE a.legalCase.uuid = :uuid")
	Set<UUID> findAllByLegalCaseUuid(UUID uuid);

}
