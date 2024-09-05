package pl.lawit.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.model.CompanyNip;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.web.dto.CompanyDto;

import java.util.UUID;

import static pl.lawit.domain.command.CompanyCommand.AddLawyerToCompany;
import static pl.lawit.domain.command.CompanyCommand.CreateCompany;
import static pl.lawit.domain.command.CompanyCommand.DeleteCompanyLawyer;
import static pl.lawit.domain.command.CompanyCommand.UpdateCompany;

@Component
@RequiredArgsConstructor
public class CompanyCommandMapper {

	private final AuthenticatedUserResolver authenticatedUserResolver;

	public CreateCompany mapToCreateCompanyCommand(CompanyDto.CreateCompanyRequestDto dto) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		return CreateCompany.builder()
			.companyName(dto.companyName())
			.nip(CompanyNip.of(dto.nip()))
			.companyEmail(EmailAddress.of(dto.companyEmail()))
			.authenticatedUser(authenticatedUser)
			.build();
	}

	public UpdateCompany mapToUpdateCompanyCommand(UUID companyUuid, CompanyDto.UpdateCompanyRequestDto dto) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		return UpdateCompany.builder()
			.companyUuid(companyUuid)
			.companyName(dto.companyName())
			.nip(CompanyNip.of(dto.nip()))
			.companyEmail(EmailAddress.of(dto.companyEmail()))
			.authenticatedUser(authenticatedUser)
			.build();
	}

	public AddLawyerToCompany mapToAddUserToCompanyCommand(UUID companyUuid, UUID userUuid) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		return AddLawyerToCompany.builder()
			.companyUuid(companyUuid)
			.lawyerUuid(userUuid)
			.authenticatedUser(authenticatedUser)
			.build();
	}

	public DeleteCompanyLawyer mapToDeleteCompanyMemberCommand(UUID companyUuid, UUID memberUuid) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		return DeleteCompanyLawyer.builder()
			.companyUuid(companyUuid)
			.lawyerUuid(memberUuid)
			.authenticatedUser(authenticatedUser)
			.build();
	}
}
