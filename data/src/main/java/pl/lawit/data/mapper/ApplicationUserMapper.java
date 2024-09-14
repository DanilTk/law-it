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
	@Mapping(target = "idpUid", source = "idpUid")
	public abstract ApplicationUser mapToDomain(ApplicationUserEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "idpUid", source = "idpUid")
	@Mapping(target = "email", source = "email.value")
	@Mapping(target = "isIdpUser", constant = "true")
	@Mapping(target = "lastSyncAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "lastLoginAt", ignore = true)
	public abstract ApplicationUserEntity mapToEntity(String idpUid, EmailAddress email);

}
