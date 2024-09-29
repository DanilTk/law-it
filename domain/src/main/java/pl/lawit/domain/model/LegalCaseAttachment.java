package pl.lawit.domain.model;

import io.vavr.control.Option;
import lombok.NonNull;
import pl.lawit.kernel.repository.BaseModel;

import java.time.Instant;
import java.util.UUID;

public record LegalCaseAttachment(

	@NonNull
	UUID uuid,

	@NonNull
	UUID legalCaseUuid,

	@NonNull
	UUID fileUuid,

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
