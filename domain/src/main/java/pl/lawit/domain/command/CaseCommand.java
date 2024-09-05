package pl.lawit.domain.command;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.domain.model.CaseStatus;
import pl.lawit.domain.model.CaseType;
import pl.lawit.kernel.authentication.AuthenticatedUser;

import java.util.UUID;

public interface CaseCommand {

	@Builder(toBuilder = true)
	record CreateCase(

		@NonNull
		String title,

		@NonNull
		String description,

		@NonNull
		CaseStatus caseStatus,

		@NonNull
		CaseType caseType,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record UpdateCase(

		@NonNull
		UUID caseUuid,

		@NonNull
		String title,

		@NonNull
		String description,

		@NonNull
		Option<UUID> companyUuid,

		@NonNull
		Option<UUID> lawyerUuid,

		@NonNull
		CaseStatus caseStatus,

		@NonNull
		CaseType caseType,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

}
