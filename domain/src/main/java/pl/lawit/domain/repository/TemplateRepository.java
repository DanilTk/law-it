package pl.lawit.domain.repository;

import io.vavr.collection.List;
import pl.lawit.domain.model.DocumentTemplate;
import pl.lawit.kernel.repository.BaseRepository;

import java.util.UUID;

import static pl.lawit.domain.command.DocumentTemplateCommand.CreateDocumentTemplate;
import static pl.lawit.domain.command.DocumentTemplateCommand.FindTemplates;

public interface TemplateRepository extends BaseRepository<DocumentTemplate> {

	DocumentTemplate create(CreateDocumentTemplate command);

	List<DocumentTemplate> findAll(FindTemplates commandQuery);

	void deleteByUuid(UUID uuid);

}
