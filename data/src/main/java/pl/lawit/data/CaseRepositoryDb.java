package pl.lawit.data;

import io.vavr.collection.List;
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
import pl.lawit.data.entity.CaseEntity;
import pl.lawit.data.entity.CompanyEntity;
import pl.lawit.data.entity.LawyerEntity;
import pl.lawit.data.factory.PageResultFactory;
import pl.lawit.data.factory.PageableFactory;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.jpa.CaseRepositoryJpa;
import pl.lawit.data.jpa.CompanyRepositoryJpa;
import pl.lawit.data.jpa.LawyerRepositoryJpa;
import pl.lawit.data.mapper.CaseMapper;
import pl.lawit.data.nosql.CaseDescriptionMongoJpa;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Case;
import pl.lawit.domain.repository.CaseRepository;
import pl.lawit.kernel.model.PageResult;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import static pl.lawit.domain.command.CaseCommand.CreateCase;
import static pl.lawit.domain.command.CaseCommand.UpdateCase;

@Repository
@RequiredArgsConstructor
public class CaseRepositoryDb implements CaseRepository {

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final CompanyRepositoryJpa companyRepositoryJpa;

	private final LawyerRepositoryJpa lawyerRepositoryJpa;

	private final CaseDescriptionMongoJpa caseDescriptionMongoJpa;

	private final CaseRepositoryJpa caseRepositoryJpa;

	private final CaseMapper caseMapper;

	private final PageResultFactory pageResultFactory;

	private final PageableFactory pageableFactory;

	@Override
	@Transactional(propagation = MANDATORY)
	public Case create(CreateCase command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());
		CaseDescriptionDocument document = caseMapper.mapToDocument(command.description());
		document = caseDescriptionMongoJpa.save(document);

		CaseEntity entity = caseMapper.mapToEntity(command, document.getUuid(), userEntity);
		entity = caseRepositoryJpa.save(entity);
		return caseMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public String getCaseDescriptionByCaseUuid(UUID uuid) {
		CaseEntity caseEntity = caseRepositoryJpa.getReferenceByUuid(uuid);
		CaseDescriptionDocument descriptionDocument = caseDescriptionMongoJpa
			.getReferenceByUuid(caseEntity.getDescriptionUuid());
		return descriptionDocument.getDescription();
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<Case> findByUserUuid(UUID uuid, PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<Case> page = caseRepositoryJpa.findAllByCreatedByUuid(uuid, pageable)
			.map(caseMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<Case> findByLawyerUuid(UUID uuid, PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<Case> page = caseRepositoryJpa.findAllByLawyerUuid(uuid, pageable)
			.map(caseMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<Case> findByCompanyUuid(UUID uuid, PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<Case> page = caseRepositoryJpa.findAllByCompanyUuid(uuid, pageable)
			.map(caseMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Case getByUuid(UUID uuid) {
		CaseEntity entity = caseRepositoryJpa.getReferenceByUuid(uuid);
		return caseMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<Case> findByUuid(UUID uuid) {
		return Option.ofOptional(caseRepositoryJpa.findById(uuid)
			.map(caseMapper::mapToDomain));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<Case> findPage(PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<Case> page = caseRepositoryJpa.findAll(pageable)
			.map(caseMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<Case> findAll() {
		return List.ofAll(caseRepositoryJpa.findAll())
			.map(caseMapper::mapToDomain);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public Case update(UpdateCase command) {
		ApplicationUserEntity userEntity =
			applicationUserRepositoryJpa.getReferenceByUuid(command.authenticatedUser().userUuid());

		CaseEntity entity = caseRepositoryJpa.getReferenceByUuid(command.caseUuid());

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

		return caseMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public void deleteByUuid(UUID uuid) {
		CaseEntity caseEntity = caseRepositoryJpa.getReferenceByUuid(uuid);
		caseDescriptionMongoJpa.deleteById(caseEntity.getDescriptionUuid());
		caseRepositoryJpa.deleteById(uuid);
	}

	private Pageable getPageable(PageCommandQuery pageQuery) {
		Sort sort = Sort.by(Sort.Direction.DESC, BaseEntity_.CREATED_AT);

		return pageableFactory.create(pageQuery, sort);
	}

}
