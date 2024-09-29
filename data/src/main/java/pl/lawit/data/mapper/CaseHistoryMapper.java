package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.LegalCaseEntity;
import pl.lawit.data.entity.LegalCaseHistoryEntity;

@Mapper(componentModel = "spring")
public abstract class CaseHistoryMapper extends BaseMapper {

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", source = "userEntity")
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "legalCase", source = "legalCaseEntity")
	@Mapping(target = "caseStatus", source = "legalCaseEntity.caseStatus")
	@Mapping(target = "lawyer", source = "legalCaseEntity.lawyer")
	@Mapping(target = "completionDeadline", source = "legalCaseEntity.completionDeadline")
	@Mapping(target = "acceptanceDeadline", source = "legalCaseEntity.acceptanceDeadline")
	public abstract LegalCaseHistoryEntity mapToEntity(LegalCaseEntity legalCaseEntity,
													   ApplicationUserEntity userEntity);

}
