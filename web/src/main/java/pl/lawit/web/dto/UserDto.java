package pl.lawit.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public interface UserDto {

	@Builder(toBuilder = true)
	record UserResponseDto(

		@Schema(description = "The id of the user", requiredMode = NOT_REQUIRED)
		@Nullable
		Option<UUID> id,

		@Schema(description = "Email address of the user", requiredMode = REQUIRED)
		@NonNull
		String email,

		@Schema(description = "Flag indicating if the user has an account in IDP", requiredMode = REQUIRED)
		boolean isActiveAccount,

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
		@Schema(description = "The date and time when the user was created", requiredMode = REQUIRED)
		@NonNull
		Instant createdAt

	) {
	}

}