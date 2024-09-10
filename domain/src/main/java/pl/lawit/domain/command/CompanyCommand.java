package pl.lawit.domain.command;

import lombok.Builder;
import lombok.NonNull;
import pl.lawit.domain.model.CompanyNip;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.EmailAddress;

import java.util.UUID;

public interface CompanyCommand {

	@Builder(toBuilder = true)
	record CreateCompany(

		@NonNull
		String companyName,

		@NonNull
		CompanyNip nip,

		@NonNull
		EmailAddress companyEmail,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record UpdateCompany(

		@NonNull
		UUID companyUuid,

		@NonNull
		String companyName,

		@NonNull
		CompanyNip nip,

		@NonNull
		EmailAddress companyEmail,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record AddLawyerToCompany(

		@NonNull
		UUID companyUuid,

		@NonNull
		UUID lawyerUuid,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record DeleteCompanyLawyer(

		@NonNull
		UUID companyUuid,

		@NonNull
		UUID lawyerUuid,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

}
