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
import pl.lawit.kernel.exception.EmailAlreadyExistsException;
import pl.lawit.kernel.exception.NipAlreadyExistsException;
import pl.lawit.kernel.model.PageResult;

import java.util.UUID;

import static pl.lawit.domain.command.CompanyCommand.AddLawyerToCompany;
import static pl.lawit.domain.command.CompanyCommand.CreateCompany;
import static pl.lawit.domain.command.CompanyCommand.DeleteCompanyLawyer;
import static pl.lawit.domain.command.CompanyCommand.UpdateCompany;
import static pl.lawit.kernel.logger.ApplicationLoggerFactory.companyLogger;

@Service
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository companyRepository;

	private final LawyerRepository lawyerRepository;

	@Transactional
	public Company createCompany(CreateCompany command) {
		if (companyRepository.existsByCompanyEmail(command.companyEmail())) {
			throw new EmailAlreadyExistsException();
		}

		if (companyRepository.existsByCompanyNip(command.nip())) {
			throw new NipAlreadyExistsException();
		}

		Company company = companyRepository.create(command);
		companyLogger().info("Company created: {}", company.companyName());

		return company;
	}

	@Transactional
	public Company getCompany(UUID uuid) {
		return companyRepository.getByUuid(uuid);
	}

	@Transactional
	public Company updateCompany(UpdateCompany command) {
		Company updatedCompany = companyRepository.update(command);
		companyLogger().info("Company updated: {}", updatedCompany.companyName());
		return updatedCompany;
	}

	@Transactional
	public PageResult<Company> findCompaniesPage(PageCommandQuery commandQuery) {
		return companyRepository.findPage(commandQuery);
	}

	@Transactional
	public List<Lawyer> findAllCompanyLawyers(UUID uuid) {
		return lawyerRepository.findLawyersByCompanyUuid(uuid);
	}

	@Transactional
	public void deleteCompany(UUID companyUuid, AuthenticatedUser authenticatedUser) {
		if (lawyerRepository.existsByCompanyUuid(companyUuid)) {
			throw new IllegalStateException("Cannot delete company with lawyers");
		}

		companyRepository.deleteByUuid(companyUuid);
		companyLogger().info("Company deleted: {} by user: {}", companyUuid, authenticatedUser);
	}

	@Transactional
	public Lawyer addLawyerToCompany(AddLawyerToCompany command) {
		Lawyer lawyer = lawyerRepository.getByUuid(command.lawyerUuid());
		if (lawyer.companyUuid().isDefined()) {
			throw new IllegalStateException(String.format("Lawyer: %s already assigned to company", lawyer.uuid()));
		}

		Lawyer assignedLawyer = lawyerRepository.assignToCompany(command);
		companyLogger().info("Lawyer {} assigned to company {}", lawyer.uuid(), command.companyUuid());
		return assignedLawyer;
	}

	@Transactional
	public void deleteCompanyLawyer(DeleteCompanyLawyer command) {
		Lawyer lawyer = lawyerRepository.getByUuid(command.lawyerUuid());
		if (lawyer.companyUuid().isDefined() && lawyer.companyUuid().get().equals(command.companyUuid())) {
			lawyerRepository.unassignFromCompany(command.lawyerUuid(), command.authenticatedUser());
			companyLogger().info("Lawyer {} unassigned from company {}", lawyer.uuid(), command.companyUuid());
		}
	}
}
