package pl.lawit.web.mapper;

import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.Lawyer;

import static pl.lawit.web.dto.LawyerDto.LawyerResponseDto;

@UtilityClass
public final class LawyerDtoMapper {

	public static LawyerResponseDto map(Lawyer lawyer) {
		return LawyerResponseDto.builder()
			.id(lawyer.uuid())
			.companyId(lawyer.companyUuid())
			.hourlyRate(lawyer.hourlyRate().value())
			.pesel(lawyer.pesel().value())
			.build();
	}
}
