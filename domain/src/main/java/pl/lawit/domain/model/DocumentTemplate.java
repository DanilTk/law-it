package pl.lawit.domain.model;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.repository.BaseModel;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record DocumentTemplate(

	@NonNull
	UUID uuid,

	@NonNull
	TemplateCategory templateCategory,

	@NonNull
	Language languageCode,

	@NonNull
	String templateName,

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
