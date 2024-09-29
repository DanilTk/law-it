package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.document.CaseDescriptionDocument;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.LegalCaseEntity;
import pl.lawit.domain.command.CaseCommand.CreateCase;
import pl.lawit.domain.model.LegalCase;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class LegalCaseMapper extends BaseMapper {

	@Mapping(target = "uuid", source = "uuid")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", expression = "java(ofOption(entity.getUpdatedAt()))")
	@Mapping(target = "createdBy", expression = "java(extractUserUuid(entity.getCreatedBy()))")
	@Mapping(target = "updatedBy", expression = "java(ofOption(entity.getUpdatedBy()).map($->$.getUuid()))")
	@Mapping(target = "title", expression = "java(entity.getTitle())")
	@Mapping(target = "descriptionUuid", expression = "java(entity.getDescriptionUuid())")
	@Mapping(target = "lawyerUuid", expression = "java(ofOption(entity.getLawyer()).map($->$.getUuid()))")
	@Mapping(target = "companyUuid", expression = "java(ofOption(entity.getCompany()).map($->$.getUuid()))")
	@Mapping(target = "completionDeadline", expression = "java(ofOption(entity.getCompletionDeadline()))")
	@Mapping(target = "acceptanceDeadline", expression = "java(ofOption(entity.getAcceptanceDeadline()))")
	@Mapping(target = "caseType", expression = "java(entity.getCaseType())")
	@Mapping(target = "caseStatus", expression = "java(entity.getCaseStatus())")
	@Mapping(target = "caseCategory", expression = "java(entity.getCaseCategory())")
	public abstract LegalCase mapToDomain(LegalCaseEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", source = "userEntity")
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "title", source = "command.title")
	@Mapping(target = "descriptionUuid", source = "description")
	@Mapping(target = "caseType", source = "command.caseType")
	@Mapping(target = "caseStatus", source = "command.caseStatus")
	@Mapping(target = "caseCategory", source = "command.caseCategory")
	@Mapping(target = "company", ignore = true)
	@Mapping(target = "lawyer", ignore = true)
	@Mapping(target = "completionDeadline", ignore = true)
	@Mapping(target = "acceptanceDeadline", ignore = true)
	public abstract LegalCaseEntity mapToEntity(CreateCase command, UUID description,
												ApplicationUserEntity userEntity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "description", source = "description")
	public abstract CaseDescriptionDocument mapToDocument(String description);

}
