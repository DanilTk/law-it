package pl.lawit.data;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.DocumentTemplateEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.jpa.DocumentTemplateRepositoryJpa;
import pl.lawit.data.jpa.RegisteredFileRepositoryJpa;
import pl.lawit.data.jpa.UserTemplateRepositoryJpa;
import pl.lawit.data.mapper.DocumentTemplateMapper;
import pl.lawit.data.specification.TemplateSpecificationFactory;
import pl.lawit.domain.model.DocumentTemplate;
import pl.lawit.domain.repository.TemplateRepository;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import static pl.lawit.domain.command.DocumentTemplateCommand.CreateDocumentTemplate;
import static pl.lawit.domain.command.DocumentTemplateCommand.FindTemplates;

@Repository
@RequiredArgsConstructor
public class TemplateRepositoryDb implements TemplateRepository {

	private final UserTemplateRepositoryJpa userTemplateRepositoryJpa;

	private final DocumentTemplateRepositoryJpa documentTemplateRepositoryJpa;

	private final RegisteredFileRepositoryJpa registeredFileRepositoryJpa;

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final DocumentTemplateMapper documentTemplateMapper;

	private final TemplateSpecificationFactory specificationFactory;

	@Override
	@Transactional(propagation = MANDATORY)
	public DocumentTemplate create(CreateDocumentTemplate command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());

		RegisteredFileEntity fileEntity = registeredFileRepositoryJpa.getReferenceByUuid(command.fileUuid());

		DocumentTemplateEntity entity = documentTemplateMapper.mapToEntity(command, fileEntity, userEntity);

		entity = documentTemplateRepositoryJpa.save(entity);

		return documentTemplateMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public DocumentTemplate getByUuid(UUID uuid) {
		DocumentTemplateEntity entity = documentTemplateRepositoryJpa.getReferenceByUuid(uuid);
		return documentTemplateMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<DocumentTemplate> findByUuid(UUID uuid) {
		return Option.ofOptional(documentTemplateRepositoryJpa.findById(uuid)
			.map(documentTemplateMapper::mapToDomain));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<DocumentTemplate> findAll() {
		return List.ofAll(documentTemplateRepositoryJpa.findAll())
			.map(documentTemplateMapper::mapToDomain);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<DocumentTemplate> findAll(FindTemplates commandQuery) {
		Specification<DocumentTemplateEntity> specification = specificationFactory.findCompanies(commandQuery);
		return List.ofAll(documentTemplateRepositoryJpa.findAll(specification))
			.map(documentTemplateMapper::mapToDomain);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public void deleteByUuid(UUID uuid) {
		documentTemplateRepositoryJpa.deleteById(uuid);
	}

}
