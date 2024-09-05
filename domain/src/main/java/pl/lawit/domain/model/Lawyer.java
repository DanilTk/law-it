package pl.lawit.domain.model;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.model.MoneyAmount;
import pl.lawit.kernel.repository.BaseModel;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Lawyer(

	@NonNull
	UUID uuid,

	@NonNull
	UUID userUuid,

	@NonNull
	Option<UUID> companyUuid,

	@NonNull
	UUID cetificateUuid,

	@NonNull
	Pesel pesel,

	@NonNull
	MoneyAmount hourlyRate,

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
