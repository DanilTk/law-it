package pl.lawit.web.mapper;

import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.DocumentTemplate;

import static pl.lawit.web.dto.TemplateDto.TemplateResponseDto;

@UtilityClass
public final class TemplateDtoMapper {

	public static TemplateResponseDto map(DocumentTemplate documentTemplate) {
		return TemplateResponseDto.builder()
			.id(documentTemplate.uuid())
			.templateCategory(documentTemplate.templateCategory())
			.templateLanguage(documentTemplate.languageCode())
			.templateName(documentTemplate.templateName())
			.createdAt(documentTemplate.createdAt())
			.build();
	}

}
