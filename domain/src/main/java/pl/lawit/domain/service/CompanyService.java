package pl.lawit.domain.service;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Company;
import pl.lawit.domain.model.Lawyer;
import pl.lawit.domain.repository.CompanyRepository;
import pl.lawit.domain.repository.LawyerRepository;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.PageResult;

import java.util.UUID;

import static pl.lawit.domain.command.CompanyCommand.AddLawyerToCompany;
import static pl.lawit.domain.command.CompanyCommand.CreateCompany;
import static pl.lawit.domain.command.CompanyCommand.DeleteCompanyLawyer;
import static pl.lawit.domain.command.CompanyCommand.UpdateCompany;

@Service
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository companyRepository;

	private final LawyerRepository lawyerRepository;

	@Transactional
	public Company createCompany(CreateCompany command) {
		return null;
	}

	@Transactional
	public Company getCompany(UUID uuid) {
		return null;
	}

	@Transactional
	public Company updateCompany(UpdateCompany command) {
		return null;
	}

	@Transactional
	public PageResult<Company> findCompaniesPage(PageCommandQuery commandQuery) {
		return null;
	}

	@Transactional
	public List<Lawyer> findAllCompanyLawyers(UUID uuid) {
		return null;
	}

	@Transactional
	public void deleteCompany(UUID companyUuid, AuthenticatedUser authenticatedUser) {

	}

	@Transactional
	public Lawyer addLawyerToCompany(AddLawyerToCompany command) {
		return null;
	}

	@Transactional
	public void deleteCompanyLawyer(DeleteCompanyLawyer command) {
	}
}
