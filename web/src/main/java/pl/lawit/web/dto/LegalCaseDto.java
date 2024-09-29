package pl.lawit.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vavr.control.Option;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import pl.lawit.domain.model.CaseStatus;
import pl.lawit.domain.model.CaseType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static pl.lawit.web.dto.PaymentOrderDto.PaymentOrderResponseDto;

public interface LegalCaseDto {

	@Builder(toBuilder = true)
	record LegalCaseResponseDto(

		@Schema
		@NonNull
		UUID id,

		@NonNull
		String title,

		@NonNull
		CaseType caseType,

		@NonNull
		CaseStatus caseStatus,

		@NonNull
		Option<Instant> completionDeadline,

		@NonNull
		Option<Instant> acceptanceDeadline,

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
		@Schema(description = "The date and time when the case was created")
		@NonNull
		Instant createdAt

	) {
	}

	@Builder(toBuilder = true)
	record LegalCaseDetailResponseDto(

		@NonNull
		LegalCaseResponseDto legalCase,

		@NonNull
		PaymentOrderResponseDto paymentOrder

	) {
	}

	@Builder(toBuilder = true)
	record CreateBasicCaseRequestDto(

		@Schema
		@Length(min = 5, max = 100)
		@NotBlank
		String title,

		@Schema
		@Length(min = 100, max = 500)
		@NonNull
		String description,

		@NonNull
		CaseType caseType

	) {
	}

	@Builder(toBuilder = true)
	record CreateAdvancedCaseRequestDto(

		@Schema
		@Length(min = 5, max = 100)
		@NotBlank
		String title,

		@Schema
		@Length(min = 100, max = 1000)
		@NonNull
		String description,

		@NonNull
		CaseType caseType,

		@Size(max = 5)
		@NonNull
		Set<UUID> fileIds

	) {
	}
}
