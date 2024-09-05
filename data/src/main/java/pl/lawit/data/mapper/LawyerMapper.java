package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.LawyerEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.domain.command.LawyerCommand.CreateLawyer;
import pl.lawit.domain.model.Lawyer;

@Mapper(componentModel = "spring")
public abstract class LawyerMapper extends BaseMapper {

	@Mapping(target = "uuid", source = "uuid")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", expression = "java(ofOption(entity.getUpdatedAt()))")
	@Mapping(target = "createdBy", expression = "java(extractUserUuid(entity.getCreatedBy()))")
	@Mapping(target = "updatedBy", expression = "java(ofOption(entity.getUpdatedBy()).map($->$.getUuid()))")
	@Mapping(target = "userUuid", expression = "java(entity.getApplicationUser().getUuid())")
	@Mapping(target = "companyUuid", expression = "java(ofOption(entity.getCompany()).map($->$.getUuid()))")
	@Mapping(target = "cetificateUuid", expression = "java(entity.getCertificate().getUuid())")
	@Mapping(target = "pesel", expression = "java(Pesel.of(entity.getPesel()))")
	@Mapping(target = "hourlyRate", expression = "java(MoneyAmount.of(entity.getHourlyRate()))")
	public abstract Lawyer mapToDomain(LawyerEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", source = "userEntity")
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "hourlyRate", source = "command.hourlyRate.value")
	@Mapping(target = "pesel", source = "command.pesel.value")
	@Mapping(target = "certificate", source = "registeredFileEntity")
	@Mapping(target = "applicationUser", source = "applicationUserEntity")
	@Mapping(target = "company", ignore = true)
	public abstract LawyerEntity mapToEntity(CreateLawyer command, ApplicationUserEntity applicationUserEntity,
											 ApplicationUserEntity userEntity,
											 RegisteredFileEntity registeredFileEntity);

}
