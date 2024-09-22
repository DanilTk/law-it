package pl.lawit.web.mapper;

import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.PurchasedDocumentInfo;

import static pl.lawit.web.dto.TemplateDto.PurchasedDocumentResponseDto;

@UtilityClass
public final class PurchasedDocumentDtoMapper {

	public static PurchasedDocumentResponseDto map(PurchasedDocumentInfo info) {
		return PurchasedDocumentResponseDto.builder()
			.id(info.purchasedDocument().uuid())
			.documentName(info.purchasedDocumentFile().originalFileName().value())
			.templateCategory(info.documentTemplate().templateCategory())
			.templateLanguage(info.documentTemplate().languageCode())
			.purchasedAt(info.purchasedDocument().createdAt())
			.build();
	}

}
