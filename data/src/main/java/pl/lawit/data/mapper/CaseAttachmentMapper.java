package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.CaseAttachmentEntity;
import pl.lawit.data.entity.LegalCaseEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.domain.model.LegalCaseAttachment;

@Mapper(componentModel = "spring")
public abstract class CaseAttachmentMapper extends BaseMapper {

	@Mapping(target = "uuid", source = "uuid")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", expression = "java(ofOption(entity.getUpdatedAt()))")
	@Mapping(target = "createdBy", expression = "java(extractUserUuid(entity.getCreatedBy()))")
	@Mapping(target = "updatedBy", expression = "java(ofOption(entity.getUpdatedBy()).map($->$.getUuid()))")
	@Mapping(target = "legalCaseUuid", expression = "java(entity.getLegalCase().getUuid())")
	@Mapping(target = "fileUuid", expression = "java(entity.getFile().getUuid())")
	public abstract LegalCaseAttachment mapToDomain(CaseAttachmentEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", source = "userEntity")
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "file", source = "fileEntity")
	@Mapping(target = "legalCase", source = "legalCaseEntity")
	public abstract CaseAttachmentEntity mapToEntity(LegalCaseEntity legalCaseEntity, RegisteredFileEntity fileEntity,
													 ApplicationUserEntity userEntity);

}
