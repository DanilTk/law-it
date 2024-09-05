package pl.lawit.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.domain.model.CaseStatus;
import pl.lawit.domain.model.CaseType;

import java.time.Instant;
import java.util.UUID;

public interface CaseDto {

	@Builder(toBuilder = true)
	record CaseResponseDto(

		@Schema
		@NonNull
		UUID id,

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

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
		@Schema(description = "The date and time when the case was created")
		@NonNull
		Instant createdAt,

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
		@Schema(description = "The date and time when the case was updated")
		@NonNull
		Option<Instant> updatedAt

	) {
	}
}
