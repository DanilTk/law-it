package pl.lawit.domain.command;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.domain.model.Pesel;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.MoneyAmount;

import java.util.UUID;

public interface LawyerCommand {

	@Builder(toBuilder = true)
	record CreateLawyer(

		@NonNull
		UUID userUuid,

		@NonNull
		Option<UUID> companyUuid,

		@NonNull
		UUID fileUuid,

		@NonNull
		Pesel pesel,

		@NonNull
		MoneyAmount hourlyRate,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record UpdateLawyerRate(

		@NonNull
		UUID uuid,

		@NonNull
		MoneyAmount hourlyRate,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record UpdateLawyerCertificate(

		@NonNull
		UUID uuid,

		@NonNull
		UUID fileUuid,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record AssignLawyerToCompany(

		@NonNull
		UUID uuid,

		@NonNull
		UUID companyUuid,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

}
