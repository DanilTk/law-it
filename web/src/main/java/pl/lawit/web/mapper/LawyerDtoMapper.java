package pl.lawit.web.mapper;

import pl.lawit.domain.model.Lawyer;

import static pl.lawit.web.dto.LawyerDto.LawyerResponseDto;

public class LawyerDtoMapper {

	private LawyerDtoMapper() {
	}

	public static LawyerResponseDto map(Lawyer lawyer) {
		return LawyerResponseDto.builder()
			.id(lawyer.uuid())
			.companyId(lawyer.companyUuid())
			.hourlyRate(lawyer.hourlyRate().value())
			.pesel(lawyer.pesel().value())
			.build();
	}
}
