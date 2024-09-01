package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.model.EmailAddress;

@Mapper(componentModel = "spring")
public abstract class ApplicationUserMapper extends BaseMapper {

	@Mapping(target = "uuid", expression = "java(ofOption(entity.getUuid()))")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "email", expression = "java(EmailAddress.of(entity.getEmail()))")
	@Mapping(target = "isIdpUser", source = "isIdpUser")
	@Mapping(target = "idpSub", source = "idpSub")
	public abstract ApplicationUser mapToDomain(ApplicationUserEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "idpSub", source = "idpSub")
	@Mapping(target = "email", source = "email.value")
	@Mapping(target = "isIdpUser", constant = "true")
	@Mapping(target = "lastSyncAt", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "lastLoginAt", ignore = true)
	public abstract ApplicationUserEntity mapToEntity(String idpSub, EmailAddress email);

}
