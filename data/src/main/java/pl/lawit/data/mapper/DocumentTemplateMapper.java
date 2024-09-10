package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.DocumentTemplateEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.domain.model.DocumentTemplate;

import static pl.lawit.domain.command.DocumentTemplateCommand.CreateDocumentTemplate;

@Mapper(componentModel = "spring")
public abstract class DocumentTemplateMapper extends BaseMapper {

	@Mapping(target = "uuid", source = "uuid")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", expression = "java(ofOption(entity.getUpdatedAt()))")
	@Mapping(target = "createdBy", expression = "java(extractUserUuid(entity.getCreatedBy()))")
	@Mapping(target = "updatedBy", expression = "java(ofOption(entity.getUpdatedBy()).map($->$.getUuid()))")
	@Mapping(target = "templateCategory", expression = "java(entity.getTemplateCategory())")
	@Mapping(target = "fileUuid", expression = "java(entity.getFile().getUuid())")
	@Mapping(target = "languageCode", expression = "java(entity.getLanguageCode())")
	@Mapping(target = "templateName", expression = "java(entity.getTemplateName())")
	public abstract DocumentTemplate mapToDomain(DocumentTemplateEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", source = "userEntity")
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "templateCategory", source = "command.templateCategory")
	@Mapping(target = "file", source = "fileEntity")
	@Mapping(target = "languageCode", source = "command.languageCode")
	@Mapping(target = "templateName", source = "command.fileName")
	public abstract DocumentTemplateEntity mapToEntity(CreateDocumentTemplate command,
													   RegisteredFileEntity fileEntity,
													   ApplicationUserEntity userEntity);

}
