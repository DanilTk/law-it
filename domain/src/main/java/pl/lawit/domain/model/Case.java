package pl.lawit.domain.model;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.repository.BaseModel;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Case(

	@NonNull
	UUID uuid,

	@NonNull
	String title,

	@NonNull
	UUID descriptionUuid,

	@NonNull
	Option<UUID> lawyerUuid,

	@NonNull
	Option<UUID> companyUuid,

	@NonNull
	CaseType caseType,

	@NonNull
	CaseStatus caseStatus,

	@NonNull
	Instant createdAt,

	@NonNull
	Option<Instant> updatedAt,

	@NonNull
	UUID createdBy,

	@NonNull
	Option<UUID> updatedBy

) implements BaseModel {
}
