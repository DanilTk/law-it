package pl.lawit.web.mapper;

import pl.lawit.domain.model.Company;

import static pl.lawit.web.dto.CompanyDto.CompanyResponseDto;

public class CompanyDtoMapper {

	private CompanyDtoMapper() {
	}

	public static CompanyResponseDto map(Company company) {
		return CompanyResponseDto.builder()
			.id(company.uuid())
			.companyName(company.companyName())
			.companyEmail(company.companyEmail().value())
			.nip(company.nip().value())
			.createdAt(company.createdAt())
			.build();
	}

}
