package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.CompanyEntity;
import pl.lawit.domain.model.Company;

import static pl.lawit.domain.command.CompanyCommand.CreateCompany;

@Mapper(componentModel = "spring")
public abstract class CompanyMapper extends BaseMapper {

	@Mapping(target = "uuid", source = "uuid")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", expression = "java(ofOption(entity.getUpdatedAt()))")
	@Mapping(target = "createdBy", expression = "java(extractUserUuid(entity.getCreatedBy()))")
	@Mapping(target = "updatedBy", expression = "java(ofOption(entity.getUpdatedBy()).map($->$.getUuid()))")
	@Mapping(target = "companyName", expression = "java(entity.getCompanyName())")
	@Mapping(target = "companyEmail", expression = "java(EmailAddress.of(entity.getCompanyEmail()))")
	@Mapping(target = "nip", expression = "java(CompanyNip.of(entity.getNip()))")
	public abstract Company mapToDomain(CompanyEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", source = "userEntity")
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "companyName", source = "command.companyName")
	@Mapping(target = "nip", source = "command.nip.value")
	@Mapping(target = "companyEmail", source = "command.companyEmail.value")
	public abstract CompanyEntity mapToEntity(CreateCompany command, ApplicationUserEntity userEntity);

}
