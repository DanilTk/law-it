package pl.lawit.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

public interface CompanyDto {

	@Builder(toBuilder = true)
	record CompanyResponseDto(

		@Schema(description = "The id of the company")
		@NonNull
		UUID id,

		@Schema(description = "The name of the company (1-50 characters)")
		@Size(min = 1, max = 50, message = "Company name must be between 1 and 50 characters")
		@NonNull
		String companyName,

		@Schema(description = "Company NIP")
		@NotNull
		String nip,

		@Schema(description = "Email address of the company")
		@NotNull
		String companyEmail,

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
		@Schema(description = "The date and time when the company was created")
		@NonNull
		Instant createdAt

	) {
	}

	@Builder(toBuilder = true)
	record CreateCompanyRequestDto(

		@Schema(description = "The name of the company (1-50 characters)")
		@Size(min = 1, max = 50, message = "Company name must be between 1 and 50 characters")
		String companyName,

		@Schema(description = "The type of the company")
		@NotNull
		String nip,

		@Schema(description = "Email address of the company")
		@NotNull
		String companyEmail

	) {
	}

	@Builder(toBuilder = true)
	record UpdateCompanyRequestDto(

		@Schema(description = "The name of the company (1-50 characters)")
		@Size(min = 1, max = 50, message = "Company name must be between 1 and 50 characters")
		@NotNull
		String companyName,

		@Schema(description = "Company NIP")
		@NotNull
		String nip,

		@Schema(description = "Email address of the company")
		@NotNull
		String companyEmail

	) {
	}

}
