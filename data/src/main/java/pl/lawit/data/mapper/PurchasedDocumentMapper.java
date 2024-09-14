package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.DocumentTemplateEntity;
import pl.lawit.data.entity.PurchasedDocumentEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.domain.model.PurchasedDocument;

@Mapper(componentModel = "spring")
public abstract class PurchasedDocumentMapper extends BaseMapper {

	@Mapping(target = "uuid", source = "uuid")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", expression = "java(ofOption(entity.getUpdatedAt()))")
	@Mapping(target = "createdBy", expression = "java(extractUserUuid(entity.getCreatedBy()))")
	@Mapping(target = "updatedBy", expression = "java(ofOption(entity.getUpdatedBy()).map($->$.getUuid()))")
	@Mapping(target = "fileUuid", expression = "java(entity.getPurchasedFile().getUuid())")
	@Mapping(target = "templateUuid", expression = "java(entity.getDocumentTemplate().getUuid())")
	public abstract PurchasedDocument mapToDomain(PurchasedDocumentEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", source = "userEntity")
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "purchasedFile", source = "fileEntity")
	@Mapping(target = "documentTemplate", source = "templateEntity")
	public abstract PurchasedDocumentEntity mapToEntity(ApplicationUserEntity userEntity,
														RegisteredFileEntity fileEntity,
														DocumentTemplateEntity templateEntity);

}
