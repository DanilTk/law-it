package pl.lawit.data;

import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.data.document.CaseDescriptionDocument;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.BaseEntity_;
import pl.lawit.data.entity.CaseAttachmentEntity;
import pl.lawit.data.entity.CompanyEntity;
import pl.lawit.data.entity.LawyerEntity;
import pl.lawit.data.entity.LegalCaseEntity;
import pl.lawit.data.entity.LegalCaseHistoryEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.data.factory.PageResultFactory;
import pl.lawit.data.factory.PageableFactory;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.jpa.CompanyRepositoryJpa;
import pl.lawit.data.jpa.LawyerRepositoryJpa;
import pl.lawit.data.jpa.LegalCaseAttachmentRepositoryJpa;
import pl.lawit.data.jpa.LegalCaseHistoryRepositoryJpa;
import pl.lawit.data.jpa.LegalCaseRepositoryJpa;
import pl.lawit.data.jpa.RegisteredFileRepositoryJpa;
import pl.lawit.data.mapper.CaseAttachmentMapper;
import pl.lawit.data.mapper.CaseHistoryMapper;
import pl.lawit.data.mapper.LegalCaseMapper;
import pl.lawit.data.nosql.CaseDescriptionMongoJpa;
import pl.lawit.domain.command.CaseCommand.CreateCaseHistory;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.LegalCase;
import pl.lawit.domain.repository.LegalCaseRepository;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.PageResult;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import static pl.lawit.domain.command.CaseCommand.AcceptLegalCase;
import static pl.lawit.domain.command.CaseCommand.CreateCase;
import static pl.lawit.domain.command.CaseCommand.CreateFileAttachments;
import static pl.lawit.domain.command.CaseCommand.SubmitLegalCase;
import static pl.lawit.domain.command.CaseCommand.UpdateCase;
import static pl.lawit.domain.model.CaseStatus.CANCELLED;

@Repository
@RequiredArgsConstructor
public class LegalCaseRepositoryDb implements LegalCaseRepository {

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final RegisteredFileRepositoryJpa registeredFileRepositoryJpa;

	private final LegalCaseAttachmentRepositoryJpa legalCaseAttachmentRepositoryJpa;

	private final CompanyRepositoryJpa companyRepositoryJpa;

	private final LegalCaseHistoryRepositoryJpa legalCaseHistoryRepositoryJpa;

	private final LawyerRepositoryJpa lawyerRepositoryJpa;

	private final CaseDescriptionMongoJpa caseDescriptionMongoJpa;

	private final LegalCaseRepositoryJpa legalCaseRepositoryJpa;

	private final LegalCaseMapper legalCaseMapper;

	private final CaseHistoryMapper caseHistoryMapper;

	private final CaseAttachmentMapper caseAttachmentMapper;

	private final PageResultFactory pageResultFactory;

	@Override
	@Transactional(propagation = MANDATORY)
	public LegalCase create(CreateCase command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());
		CaseDescriptionDocument document = legalCaseMapper.mapToDocument(command.description());
		document = caseDescriptionMongoJpa.save(document);

		LegalCaseEntity entity = legalCaseMapper.mapToEntity(command, document.getUuid(), userEntity);
		entity = legalCaseRepositoryJpa.save(entity);
		return legalCaseMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public Set<UUID> saveCaseAttachments(CreateFileAttachments command) {
		LegalCaseEntity legalCaseEntity = legalCaseRepositoryJpa.getReferenceByUuid(command.legalCaseUuid());
		Set<RegisteredFileEntity> fileEntities = registeredFileRepositoryJpa.findAllByUuidIn(command.fileUuids());
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());
		Set<CaseAttachmentEntity> entities = fileEntities
			.map($ -> caseAttachmentMapper
				.mapToEntity(legalCaseEntity, $, userEntity));

		return List.ofAll(legalCaseAttachmentRepositoryJpa.saveAll(entities))
			.map(CaseAttachmentEntity::getUuid)
			.toSet();
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Set<UUID> getCaseAttachments(UUID uuid) {
		return legalCaseAttachmentRepositoryJpa.findAllByLegalCaseUuid(uuid);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public void createCaseHistory(CreateCaseHistory command) {
		LegalCaseEntity legalCaseEntity = legalCaseRepositoryJpa.getReferenceByUuid(command.legalCaseUuid());
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());
		LegalCaseHistoryEntity entity = caseHistoryMapper.mapToEntity(legalCaseEntity, userEntity);
		legalCaseHistoryRepositoryJpa.save(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public String getCaseDescriptionByCaseUuid(UUID uuid) {
		LegalCaseEntity legalCaseEntity = legalCaseRepositoryJpa.getReferenceByUuid(uuid);
		CaseDescriptionDocument descriptionDocument = caseDescriptionMongoJpa
			.getReferenceByUuid(legalCaseEntity.getDescriptionUuid());
		return descriptionDocument.getDescription();
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<LegalCase> findByUserUuid(UUID uuid, PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<LegalCase> page = legalCaseRepositoryJpa.findAllByCreatedByUuid(uuid, pageable)
			.map(legalCaseMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<LegalCase> findLawyerCases(UUID uuid, PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<LegalCase> page = legalCaseRepositoryJpa.findAllByLawyer_ApplicationUserUuid(uuid, pageable)
			.map(legalCaseMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<LegalCase> findByCompanyUuid(UUID uuid, PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<LegalCase> page = legalCaseRepositoryJpa.findAllByCompanyUuid(uuid, pageable)
			.map(legalCaseMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public LegalCase getByUuid(UUID uuid) {
		LegalCaseEntity entity = legalCaseRepositoryJpa.getReferenceByUuid(uuid);
		return legalCaseMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<LegalCase> findByUuid(UUID uuid) {
		return Option.ofOptional(legalCaseRepositoryJpa.findById(uuid)
			.map(legalCaseMapper::mapToDomain));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<LegalCase> findPage(PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<LegalCase> page = legalCaseRepositoryJpa.findAll(pageable)
			.map(legalCaseMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<LegalCase> findAll() {
		return List.ofAll(legalCaseRepositoryJpa.findAll())
			.map(legalCaseMapper::mapToDomain);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public LegalCase update(UpdateCase command) {
		ApplicationUserEntity userEntity =
			applicationUserRepositoryJpa.getReferenceByUuid(command.authenticatedUser().userUuid());

		LegalCaseEntity entity = legalCaseRepositoryJpa.getReferenceByUuid(command.caseUuid());

		CaseDescriptionDocument descriptionDocument =
			caseDescriptionMongoJpa.getReferenceByUuid(entity.getDescriptionUuid());
		descriptionDocument.setDescription(command.description());

		entity.setTitle(command.title());
		entity.setCaseStatus(command.caseStatus());
		entity.setCaseType(command.caseType());
		entity.setUpdatedBy(userEntity);

		if (command.lawyerUuid().isDefined()) {
			LawyerEntity lawyer = lawyerRepositoryJpa.getReferenceByUuid(command.lawyerUuid().get());
			entity.setLawyer(lawyer);
		}

		if (command.lawyerUuid().isDefined()) {
			CompanyEntity companyEntity = companyRepositoryJpa.getReferenceByUuid(command.companyUuid().get());
			entity.setCompany(companyEntity);
		}

		return legalCaseMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public LegalCase submit(SubmitLegalCase command) {
		ApplicationUserEntity userEntity =
			applicationUserRepositoryJpa.getReferenceByUuid(command.authenticatedUser().userUuid());
		LegalCaseEntity legalCaseEntity = legalCaseRepositoryJpa.getReferenceByUuid(command.legalCaseUuid());

		legalCaseEntity.setCaseStatus(command.caseStatus());
		legalCaseEntity.setUpdatedBy(userEntity);
		legalCaseEntity.setAcceptanceDeadline(command.acceptanceDeadline());

		legalCaseEntity = legalCaseRepositoryJpa.save(legalCaseEntity);

		return legalCaseMapper.mapToDomain(legalCaseEntity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public LegalCase accept(AcceptLegalCase command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());
		LawyerEntity lawyerEntity = lawyerRepositoryJpa.getReferenceByUuid(command.lawyerUuid());
		LegalCaseEntity legalCaseEntity = legalCaseRepositoryJpa.getReferenceByUuid(command.legalCaseUuid());

		legalCaseEntity.setCaseStatus(command.caseStatus());
		legalCaseEntity.setLawyer(lawyerEntity);
		legalCaseEntity.setCompletionDeadline(command.completionDeadline());
		legalCaseEntity.setUpdatedBy(userEntity);

		legalCaseEntity = legalCaseRepositoryJpa.save(legalCaseEntity);

		return legalCaseMapper.mapToDomain(legalCaseEntity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public LegalCase withdraw(UUID uuid, AuthenticatedUser authenticatedUser) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(authenticatedUser.userUuid());
		LegalCaseEntity legalCaseEntity = legalCaseRepositoryJpa.getReferenceByUuid(uuid);

		legalCaseEntity.setCaseStatus(CANCELLED);
		legalCaseEntity.setLawyer(null);
		legalCaseEntity.setCompletionDeadline(null);
		legalCaseEntity.setAcceptanceDeadline(null);
		legalCaseEntity.setUpdatedBy(userEntity);

		legalCaseEntity = legalCaseRepositoryJpa.save(legalCaseEntity);

		return legalCaseMapper.mapToDomain(legalCaseEntity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public void deleteByUuid(UUID uuid) {
		LegalCaseEntity legalCaseEntity = legalCaseRepositoryJpa.getReferenceByUuid(uuid);
		caseDescriptionMongoJpa.deleteById(legalCaseEntity.getDescriptionUuid());
		legalCaseRepositoryJpa.deleteById(uuid);
	}

	private Pageable getPageable(PageCommandQuery commandQuery) {
		Sort sort = Sort.by(Sort.Direction.DESC, BaseEntity_.CREATED_AT);

		return PageableFactory.create(commandQuery, sort);
	}

}
