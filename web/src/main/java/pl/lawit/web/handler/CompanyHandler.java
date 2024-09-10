package pl.lawit.web.handler;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.command.CompanyCommand.AddLawyerToCompany;
import pl.lawit.domain.command.CompanyCommand.CreateCompany;
import pl.lawit.domain.command.CompanyCommand.DeleteCompanyLawyer;
import pl.lawit.domain.command.CompanyCommand.UpdateCompany;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Company;
import pl.lawit.domain.model.Lawyer;
import pl.lawit.domain.service.CompanyService;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.web.dto.CompanyDto.CompanyResponseDto;
import pl.lawit.web.dto.CompanyDto.CreateCompanyRequestDto;
import pl.lawit.web.dto.CompanyDto.UpdateCompanyRequestDto;
import pl.lawit.web.dto.ListResponseDto;
import pl.lawit.web.dto.PageResponseDto;
import pl.lawit.web.dto.PageableRequestDto;
import pl.lawit.web.factory.PageResponseDtoFactory;
import pl.lawit.web.mapper.CompanyCommandMapper;
import pl.lawit.web.mapper.CompanyDtoMapper;
import pl.lawit.web.mapper.LawyerDtoMapper;
import pl.lawit.web.mapper.PageCommandMapper;

import java.util.UUID;

import static pl.lawit.web.dto.LawyerDto.LawyerResponseDto;

@Component
@RequiredArgsConstructor
public class CompanyHandler {

	private final AuthenticatedUserResolver authenticatedUserResolver;

	private final CompanyCommandMapper commandMapper;

	private final CompanyService companyService;

	private final PageResponseDtoFactory pageResponseDtoFactory;

	public CompanyResponseDto createCompany(CreateCompanyRequestDto dto) {
		CreateCompany command = commandMapper.mapToCreateCompanyCommand(dto);
		Company company = companyService.createCompany(command);
		return CompanyDtoMapper.map(company);
	}

	public CompanyResponseDto getCompanyByUuid(UUID uuid) {
		Company company = companyService.getCompany(uuid);
		return CompanyDtoMapper.map(company);
	}

	public PageResponseDto<CompanyResponseDto> findCompaniesPage(PageableRequestDto dto) {
		PageCommandQuery commandQuery = PageCommandMapper.map(dto);
		PageResult<Company> page = companyService.findCompaniesPage(commandQuery);
		return pageResponseDtoFactory.create(page, CompanyDtoMapper::map);
	}

	public ListResponseDto<LawyerResponseDto> getCompanyLawyers(UUID uuid) {
		List<Lawyer> all = companyService.findAllCompanyLawyers(uuid);
		return ListResponseDto.of(all.map(LawyerDtoMapper::map));
	}

	public CompanyResponseDto updateCompany(UUID uuid, UpdateCompanyRequestDto dto) {
		UpdateCompany command = commandMapper.mapToUpdateCompanyCommand(uuid, dto);
		Company company = companyService.updateCompany(command);
		return CompanyDtoMapper.map(company);
	}

	public LawyerResponseDto addCompanyLawyer(UUID companyUuid, UUID lawyerUuid) {
		AddLawyerToCompany command = commandMapper.mapToAddUserToCompanyCommand(companyUuid, lawyerUuid);
		Lawyer companyMember = companyService.addLawyerToCompany(command);
		return LawyerDtoMapper.map(companyMember);
	}

	public void removeCompanyLawyer(UUID companyUuid, UUID lawyerUuid) {
		DeleteCompanyLawyer command = commandMapper.mapToDeleteCompanyMemberCommand(companyUuid, lawyerUuid);
		companyService.deleteCompanyLawyer(command);
	}

	public void deleteCompany(UUID companyUuid) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();
		companyService.deleteCompany(companyUuid, authenticatedUser);
	}
}
