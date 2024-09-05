package pl.lawit.web.mapper;

import pl.lawit.domain.model.DocumentTemplate;

import static pl.lawit.web.dto.TemplateDto.TemplateResponseDto;

public class TemplateDtoMapper {

	private TemplateDtoMapper() {
	}

	public static TemplateResponseDto map(DocumentTemplate documentTemplate) {
		return TemplateResponseDto.builder()
			.id(documentTemplate.uuid())
			.templateCategory(documentTemplate.templateCategory())
			.templateLanguage(documentTemplate.languageCode())
			.templateName(documentTemplate.templateName())
			.build();
	}

}
