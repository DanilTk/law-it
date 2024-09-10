package pl.lawit.domain.model;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.repository.BaseModel;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Company(

	@NonNull
	UUID uuid,

	@NonNull
	String companyName,

	@NonNull
	CompanyNip nip,

	@NonNull
	EmailAddress companyEmail,

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
