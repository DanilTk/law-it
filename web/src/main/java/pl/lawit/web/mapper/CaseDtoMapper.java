package pl.lawit.web.mapper;

import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.Case;

import static pl.lawit.web.dto.CaseDto.CaseResponseDto;

@UtilityClass
public final class CaseDtoMapper {

	public static CaseResponseDto map(Case legalCase) {
		return CaseResponseDto.builder()
			.id(legalCase.uuid())
			.caseStatus(legalCase.caseStatus())
			.caseType(legalCase.caseType())
			.title(legalCase.title())
			.companyUuid(legalCase.companyUuid())
			.lawyerUuid(legalCase.lawyerUuid())
			.descriptionUuid(legalCase.descriptionUuid())
			.createdAt(legalCase.createdAt())
			.updatedAt(legalCase.updatedAt())
			.build();
	}

}
