package pl.lawit.web.mapper;

import io.vavr.collection.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;
import pl.lawit.web.dto.LegalCaseDto.CreateAdvancedCaseRequestDto;

import static pl.lawit.domain.command.CaseCommand.CreateCase;
import static pl.lawit.domain.model.CaseCategory.ADVANCED;
import static pl.lawit.domain.model.CaseCategory.BASIC;
import static pl.lawit.domain.model.CaseStatus.DRAFT;
import static pl.lawit.web.dto.LegalCaseDto.CreateBasicCaseRequestDto;

@Component
@RequiredArgsConstructor
public class CaseCommandMapper {

	private final AuthenticatedUserResolver authenticatedUserResolver;

	public CreateCase mapToCreateCaseCommand(CreateBasicCaseRequestDto dto) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		return CreateCase.builder()
			.title(dto.title())
			.description(dto.description())
			.caseCategory(BASIC)
			.caseStatus(DRAFT)
			.caseType(dto.caseType())
			.fileUuids(HashSet.empty())
			.authenticatedUser(authenticatedUser)
			.build();
	}

	public CreateCase mapToCreateCaseCommand(CreateAdvancedCaseRequestDto dto) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();
		return CreateCase.builder()
			.title(dto.title())
			.description(dto.description())
			.caseCategory(ADVANCED)
			.caseStatus(DRAFT)
			.caseType(dto.caseType())
			.fileUuids(HashSet.ofAll(dto.fileIds()))
			.authenticatedUser(authenticatedUser)
			.build();
	}

}
