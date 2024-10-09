package pl.lawit.web.handler;

import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.LegalCase;
import pl.lawit.domain.model.LegalCaseInfo;
import pl.lawit.domain.service.LegalCaseService;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.web.dto.LegalCaseDto.CreateBasicCaseRequestDto;
import pl.lawit.web.dto.LegalCaseDto.LegalCaseDetailResponseDto;
import pl.lawit.web.dto.ListResponseDto;
import pl.lawit.web.dto.PageResponseDto;
import pl.lawit.web.dto.PageableRequestDto;
import pl.lawit.web.factory.PageResponseDtoFactory;
import pl.lawit.web.mapper.CaseCommandMapper;
import pl.lawit.web.mapper.CaseDtoMapper;
import pl.lawit.web.mapper.PageCommandMapper;

import java.util.UUID;

import static pl.lawit.domain.command.CaseCommand.CreateCase;
import static pl.lawit.kernel.logger.ApplicationLoggerFactory.caseLogger;
import static pl.lawit.web.dto.LegalCaseDto.CreateAdvancedCaseRequestDto;
import static pl.lawit.web.dto.LegalCaseDto.LegalCaseResponseDto;

@Component
@RequiredArgsConstructor
public class LegalCaseHandler {

	private final AuthenticatedUserResolver authenticatedUserResolver;

	private final CaseCommandMapper commandMapper;

	private final LegalCaseService legalCaseService;

	private final PageResponseDtoFactory pageResponseDtoFactory;

	public LegalCaseDetailResponseDto createBasicCase(CreateBasicCaseRequestDto dto, String clientIp) {
		CreateCase command = commandMapper.mapToCreateCaseCommand(dto, clientIp);

		LegalCaseInfo info = legalCaseService.createLegalCase(command);

		caseLogger().info("Basic case has been created");

		return CaseDtoMapper.map(info);
	}

	public LegalCaseDetailResponseDto createAdvancedCase(CreateAdvancedCaseRequestDto dto, String clientIp) {
		CreateCase command = commandMapper.mapToCreateCaseCommand(dto, clientIp);

		LegalCaseInfo info = legalCaseService.createLegalCase(command);

		return CaseDtoMapper.map(info);
	}

	public LegalCaseResponseDto getCaseByUuid(UUID uuid) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		LegalCase legalCase = legalCaseService.getCase(uuid, authenticatedUser);

		return CaseDtoMapper.map(legalCase);
	}

	public LegalCaseResponseDto acceptCase(UUID uuid) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		LegalCase legalCase = legalCaseService.acceptCase(uuid, authenticatedUser);

		caseLogger().info("Case {} has been accepted by {}", legalCase.uuid(), authenticatedUser.userUuid());

		return CaseDtoMapper.map(legalCase);
	}

	public LegalCaseResponseDto withdrawCase(UUID uuid) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		LegalCase legalCase = legalCaseService.withdrawCase(uuid, authenticatedUser);

		caseLogger().info("Case {} has been withdrawn by {}", legalCase.uuid(), authenticatedUser.userUuid());

		return CaseDtoMapper.map(legalCase);
	}

	public PageResponseDto<LegalCaseResponseDto> findCasesPage(PageableRequestDto dto) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();
		PageCommandQuery commandQuery = PageCommandMapper.map(dto);

		PageResult<LegalCase> page = legalCaseService.findLegalCasesPage(commandQuery, authenticatedUser);

		return pageResponseDtoFactory.create(page, CaseDtoMapper::map);
	}

	public String getCaseDescriptionByUuid(UUID uuid) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();
		return legalCaseService.getLegalCaseMessage(uuid, authenticatedUser);
	}

	public ListResponseDto<UUID> getCaseFilesByUuid(UUID uuid) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();
		Set<UUID> files = legalCaseService.findLegalCaseFiles(uuid, authenticatedUser);
		return ListResponseDto.of(files.toList());
	}
}
