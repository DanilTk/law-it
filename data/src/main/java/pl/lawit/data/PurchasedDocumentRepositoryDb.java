package pl.lawit.data;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.BaseEntity_;
import pl.lawit.data.entity.DocumentTemplateEntity;
import pl.lawit.data.entity.PurchasedDocumentEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.data.factory.PageResultFactory;
import pl.lawit.data.factory.PageableFactory;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.jpa.DocumentTemplateRepositoryJpa;
import pl.lawit.data.jpa.PurchasedDocumentRepositoryJpa;
import pl.lawit.data.jpa.RegisteredFileRepositoryJpa;
import pl.lawit.data.mapper.PurchasedDocumentMapper;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.command.PurchasedDocumentCommand.CreatePurchasedDocument;
import pl.lawit.domain.model.PurchasedDocument;
import pl.lawit.domain.repository.PurchasedDocumentRepository;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.PageResult;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@RequiredArgsConstructor
public class PurchasedDocumentRepositoryDb implements PurchasedDocumentRepository {

	private final PageResultFactory pageResultFactory;

	private final PurchasedDocumentRepositoryJpa purchasedDocumentRepositoryJpa;

	private final RegisteredFileRepositoryJpa registeredFileRepositoryJpa;

	private final DocumentTemplateRepositoryJpa documentTemplateRepositoryJpa;

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final PurchasedDocumentMapper purchasedDocumentMapper;

	@Override
	@Transactional(propagation = MANDATORY)
	public PurchasedDocument create(CreatePurchasedDocument command) {
		ApplicationUserEntity authenticatedUser = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());

		RegisteredFileEntity fileEntity = registeredFileRepositoryJpa.getReferenceByUuid(command.fileUuid());

		DocumentTemplateEntity documentTemplate = documentTemplateRepositoryJpa
			.getReferenceByUuid(command.templateUuid());

		PurchasedDocumentEntity entity = purchasedDocumentMapper.mapToEntity(authenticatedUser, fileEntity,
			documentTemplate);
		entity = purchasedDocumentRepositoryJpa.save(entity);

		return purchasedDocumentMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<PurchasedDocument> findAllUserDocuments(AuthenticatedUser authenticatedUser,
															  PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<PurchasedDocument> page = purchasedDocumentRepositoryJpa
			.findAllByCreatedByUuid(authenticatedUser.userUuid(), pageable)
			.map(purchasedDocumentMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PurchasedDocument getByUuid(UUID uuid) {
		PurchasedDocumentEntity entity = purchasedDocumentRepositoryJpa.getReferenceByUuid(uuid);
		return purchasedDocumentMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<PurchasedDocument> findByUuid(UUID uuid) {
		return Option.ofOptional(purchasedDocumentRepositoryJpa.findById(uuid)
			.map(purchasedDocumentMapper::mapToDomain));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<PurchasedDocument> findAll() {
		return List.ofAll(purchasedDocumentRepositoryJpa.findAll())
			.map(purchasedDocumentMapper::mapToDomain);
	}

	private Pageable getPageable(PageCommandQuery commandQuery) {
		Sort sort = Sort.by(Sort.Direction.DESC, BaseEntity_.CREATED_AT);

		return PageableFactory.create(commandQuery, sort);
	}
}
