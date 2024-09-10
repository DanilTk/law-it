package pl.lawit.domain.repository;

import pl.lawit.domain.command.CompanyCommand;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Company;
import pl.lawit.domain.model.CompanyNip;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.kernel.repository.BaseRepository;

import java.util.UUID;

public interface CompanyRepository extends BaseRepository<Company> {

	Company create(CompanyCommand.CreateCompany command);

	PageResult<Company> findPage(PageCommandQuery commandQuery);

	boolean existsByCompanyNip(CompanyNip nip);

	boolean existsByCompanyEmail(EmailAddress emailAddress);

	boolean existsByCompanyName(String companyName);

	Company update(CompanyCommand.UpdateCompany command);

	void deleteByUuid(UUID uuid);

}
