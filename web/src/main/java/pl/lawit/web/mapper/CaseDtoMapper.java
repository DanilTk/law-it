package pl.lawit.web.mapper;

import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.LegalCase;
import pl.lawit.domain.model.LegalCaseInfo;
import pl.lawit.web.dto.LegalCaseDto.LegalCaseDetailResponseDto;
import pl.lawit.web.dto.PaymentOrderDto;

import static pl.lawit.web.dto.LegalCaseDto.LegalCaseResponseDto;

@UtilityClass
public final class CaseDtoMapper {

	public static LegalCaseResponseDto map(LegalCase legalCase) {
		return LegalCaseResponseDto.builder()
			.id(legalCase.uuid())
			.caseStatus(legalCase.caseStatus())
			.caseType(legalCase.caseType())
			.title(legalCase.title())
			.acceptanceDeadline(legalCase.acceptanceDeadline())
			.completionDeadline(legalCase.completionDeadline())
			.createdAt(legalCase.createdAt())
			.build();
	}

	public static LegalCaseDetailResponseDto map(LegalCaseInfo info) {
		PaymentOrderDto.PaymentOrderResponseDto paymentOrder = PaymentDtoMapper.map(info.paymentOrder());
		LegalCaseResponseDto legalCase = map(info.legalCase());

		return LegalCaseDetailResponseDto.builder()
			.legalCase(legalCase)
			.paymentOrder(paymentOrder)
			.build();
	}

}
