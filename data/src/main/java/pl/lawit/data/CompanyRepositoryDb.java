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
import pl.lawit.data.entity.CompanyEntity;
import pl.lawit.data.entity.CompanyEntity_;
import pl.lawit.data.factory.PageResultFactory;
import pl.lawit.data.factory.PageableFactory;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.jpa.CompanyRepositoryJpa;
import pl.lawit.data.mapper.CompanyMapper;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Company;
import pl.lawit.domain.model.CompanyNip;
import pl.lawit.domain.repository.CompanyRepository;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.PageResult;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import static pl.lawit.domain.command.CompanyCommand.CreateCompany;
import static pl.lawit.domain.command.CompanyCommand.UpdateCompany;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryDb implements CompanyRepository {

	private final CompanyRepositoryJpa companyRepositoryJpa;

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final CompanyMapper companyMapper;

	private final PageableFactory pageableFactory;

	private final PageResultFactory pageResultFactory;

	@Override
	@Transactional(propagation = MANDATORY)
	public Company create(CreateCompany command) {
		ApplicationUserEntity userEntity =
			applicationUserRepositoryJpa.getReferenceByUuid(command.authenticatedUser().userUuid());
		CompanyEntity entity = companyMapper.mapToEntity(command, userEntity);
		entity = companyRepositoryJpa.save(entity);
		return companyMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Company getByUuid(UUID uuid) {
		CompanyEntity entity = companyRepositoryJpa.getReferenceByUuid(uuid);
		return companyMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<Company> findByUuid(UUID uuid) {
		return Option.ofOptional(companyRepositoryJpa.findById(uuid)
			.map(companyMapper::mapToDomain));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<Company> findPage(PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<Company> page = companyRepositoryJpa.findAll(pageable)
			.map(companyMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<Company> findAll() {
		return List.ofAll(companyRepositoryJpa.findAll())
			.map(companyMapper::mapToDomain);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public boolean existsByCompanyName(String companyName) {
		return companyRepositoryJpa.existsByCompanyName(companyName);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public boolean existsByCompanyNip(CompanyNip nip) {
		return companyRepositoryJpa.existsByNip(nip.value());
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public boolean existsByCompanyEmail(EmailAddress emailAddress) {
		return companyRepositoryJpa.existsByCompanyEmail(emailAddress.value());
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public Company update(UpdateCompany command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());

		CompanyEntity entity = companyRepositoryJpa.getReferenceByUuid(command.companyUuid());

		entity.setCompanyName(command.companyName());
		entity.setNip(command.nip().value());
		entity.setCompanyEmail(command.companyEmail().value());
		entity.setUpdatedBy(userEntity);

		return companyMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public void deleteByUuid(UUID uuid) {
		companyRepositoryJpa.deleteById(uuid);
	}

	private Pageable getPageable(PageCommandQuery pageQuery) {
		Sort sort = Sort.by(Sort.Direction.DESC, CompanyEntity_.COMPANY_NAME);

		return pageableFactory.create(pageQuery, sort);
	}
}
