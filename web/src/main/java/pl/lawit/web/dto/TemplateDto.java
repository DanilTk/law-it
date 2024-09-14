package pl.lawit.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import pl.lawit.domain.model.Language;
import pl.lawit.domain.model.TemplateCategory;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public interface TemplateDto {

	@Builder(toBuilder = true)
	record CreateTemplateRequestDto(

		@Schema
		@NonNull
		Language languageCode,

		@Schema
		@NotBlank
		@Size(min = 1, max = 50)
		String templateName,

		@Schema
		@NonNull
		TemplateCategory templateCategory,

		@Schema
		@NonNull
		UUID fileId

	) {
	}

	@Builder(toBuilder = true)
	record GenerateTemplatedDocumentRequestDto(

		@Schema
		@NonNull
		Map<String, String> data

	) {

	}

	@Getter
	@Setter
	@AllArgsConstructor
	@Builder(toBuilder = true)
	class FindTemplatesRequestDto {

		@Schema
		@Parameter
		private String templateName;

		@Schema
		@Parameter
		private java.util.Set<@NotNull TemplateCategory> templateCategories;

		@Schema
		@Parameter
		private java.util.Set<@NotNull Language> languages;

	}

	@Builder(toBuilder = true)
	record TemplateResponseDto(

		@Schema
		@NonNull
		UUID id,

		@Schema
		@NonNull
		TemplateCategory templateCategory,

		@Schema
		@NonNull
		String templateName,

		@Schema
		@NonNull
		Language templateLanguage,

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
		@Schema
		@NonNull
		Instant createdAt

	) {

	}

	@Builder(toBuilder = true)
	record PurchasedDocumentResponseDto(

		@Schema
		@NonNull
		UUID id,

		@Schema
		@NonNull
		TemplateCategory templateCategory,

		@Schema
		@NonNull
		String documentName,

		@Schema
		@NonNull
		Language templateLanguage,

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
		@Schema
		@NonNull
		Instant purchasedAt

	) {

	}
}
