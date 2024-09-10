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
import pl.lawit.data.entity.CompanyEntity;
import pl.lawit.data.entity.LawyerEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.data.factory.PageResultFactory;
import pl.lawit.data.factory.PageableFactory;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.jpa.CompanyRepositoryJpa;
import pl.lawit.data.jpa.LawyerRepositoryJpa;
import pl.lawit.data.jpa.RegisteredFileRepositoryJpa;
import pl.lawit.data.mapper.LawyerMapper;
import pl.lawit.domain.command.LawyerCommand.AssignLawyerToCompany;
import pl.lawit.domain.command.LawyerCommand.CreateLawyer;
import pl.lawit.domain.command.LawyerCommand.UpdateLawyerCertificate;
import pl.lawit.domain.command.LawyerCommand.UpdateLawyerRate;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Lawyer;
import pl.lawit.domain.model.Pesel;
import pl.lawit.domain.repository.LawyerRepository;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.PageResult;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@RequiredArgsConstructor
public class LawyerRepositoryDb implements LawyerRepository {

	private final CompanyRepositoryJpa companyRepositoryJpa;

	private final LawyerRepositoryJpa lawyerRepositoryJpa;

	private final RegisteredFileRepositoryJpa registeredFileRepositoryJpa;

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final LawyerMapper lawyerMapper;

	private final PageableFactory pageableFactory;

	private final PageResultFactory pageResultFactory;

	@Override
	@Transactional(propagation = MANDATORY)
	public Lawyer create(CreateLawyer command) {
		ApplicationUserEntity applicationUserEntity =
			applicationUserRepositoryJpa.getReferenceByUuid(command.userUuid());

		ApplicationUserEntity authenticatedUser =
			applicationUserRepositoryJpa.getReferenceByUuid(command.authenticatedUser().userUuid());

		RegisteredFileEntity fileEntity = registeredFileRepositoryJpa.getReferenceByUuid(command.fileUuid());

		LawyerEntity entity = lawyerMapper.mapToEntity(command, applicationUserEntity, authenticatedUser, fileEntity);

		entity = lawyerRepositoryJpa.save(entity);

		return lawyerMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Lawyer getByUuid(UUID uuid) {
		LawyerEntity entity = lawyerRepositoryJpa.getReferenceByUuid(uuid);
		return lawyerMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<Lawyer> findByUuid(UUID uuid) {
		return Option.ofOptional(lawyerRepositoryJpa.findById(uuid)
			.map(lawyerMapper::mapToDomain));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<Lawyer> findPage(PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<Lawyer> page = lawyerRepositoryJpa.findAll(pageable)
			.map(lawyerMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<Lawyer> findLawyersByCompanyUuid(UUID uuid) {
		return lawyerRepositoryJpa.findAllByCompanyUuid(uuid)
			.map(lawyerMapper::mapToDomain);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public boolean existsByPesel(Pesel pesel) {
		return lawyerRepositoryJpa.existsByPesel(pesel.value());
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<Lawyer> findAll() {
		return List.ofAll(lawyerRepositoryJpa.findAll())
			.map(lawyerMapper::mapToDomain);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public Lawyer update(UpdateLawyerRate command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());

		LawyerEntity entity = lawyerRepositoryJpa.getReferenceByUuid(command.uuid());

		entity.setHourlyRate(command.hourlyRate().value());
		entity.setUpdatedBy(userEntity);

		return lawyerMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public Lawyer update(UpdateLawyerCertificate command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());

		LawyerEntity entity = lawyerRepositoryJpa.getReferenceByUuid(command.uuid());

		RegisteredFileEntity fileEntity = registeredFileRepositoryJpa.getReferenceByUuid(command.fileUuid());

		entity.setCertificate(fileEntity);
		entity.setUpdatedBy(userEntity);

		return lawyerMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public Lawyer assignToCompany(AssignLawyerToCompany command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());

		LawyerEntity entity = lawyerRepositoryJpa.getReferenceByUuid(command.uuid());

		CompanyEntity companyEntity = companyRepositoryJpa.getReferenceByUuid(command.companyUuid());

		entity.setCompany(companyEntity);
		entity.setUpdatedBy(userEntity);

		return lawyerMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public Lawyer unassignFromCompany(UUID uuid, AuthenticatedUser authenticatedUser) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(authenticatedUser.userUuid());

		LawyerEntity entity = lawyerRepositoryJpa.getReferenceByUuid(uuid);

		entity.setCompany(null);
		entity.setUpdatedBy(userEntity);

		return lawyerMapper.mapToDomain(entity);
	}

	private Pageable getPageable(PageCommandQuery pageQuery) {
		Sort sort = Sort.by(Sort.Direction.DESC, BaseEntity_.CREATED_AT);

		return pageableFactory.create(pageQuery, sort);
	}
}
