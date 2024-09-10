package pl.lawit.domain.model;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.model.EmailAddress;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record ApplicationUser(

	@NonNull
	Option<UUID> uuid,

	@NonNull
	EmailAddress email,

	boolean isIdpUser,

	@NonNull
	String idpSub,

	@NonNull
	Instant createdAt

) {
}
