package pl.lawit.domain.command;

import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.domain.model.CaseCategory;
import pl.lawit.domain.model.CaseStatus;
import pl.lawit.domain.model.CaseType;
import pl.lawit.kernel.authentication.AuthenticatedUser;

import java.time.Instant;
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
		CaseCategory caseCategory,

		@NonNull
		CaseType caseType,

		@NonNull
		Set<UUID> fileUuids,

		@NonNull
		AuthenticatedUser authenticatedUser,

		@NonNull
		String clientIp

	) {
	}

	@Builder(toBuilder = true)
	record CreateCaseHistory(

		@NonNull
		UUID legalCaseUuid,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record SubmitLegalCase(

		@NonNull
		UUID legalCaseUuid,

		@NonNull
		CaseStatus caseStatus,

		@NonNull
		Instant acceptanceDeadline,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record AcceptLegalCase(

		@NonNull
		UUID legalCaseUuid,

		@NonNull
		UUID lawyerUuid,

		@NonNull
		CaseStatus caseStatus,

		@NonNull
		Instant completionDeadline,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record CreateFileAttachments(

		@NonNull
		UUID legalCaseUuid,

		@NonNull
		Set<UUID> fileUuids,

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
