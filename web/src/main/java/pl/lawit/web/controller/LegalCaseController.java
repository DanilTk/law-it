package pl.lawit.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lawit.web.dto.LegalCaseDto.CreateAdvancedCaseRequestDto;
import pl.lawit.web.dto.LegalCaseDto.LegalCaseDetailResponseDto;
import pl.lawit.web.dto.LegalCaseDto.LegalCaseResponseDto;
import pl.lawit.web.dto.ListResponseDto;
import pl.lawit.web.dto.PageResponseDto;
import pl.lawit.web.dto.PageableRequestDto;
import pl.lawit.web.handler.LegalCaseHandler;

import java.util.UUID;

import static pl.lawit.web.configuration.OpenApiConfiguration.SECURITY_SCHEME_NAME;
import static pl.lawit.web.dto.LegalCaseDto.CreateBasicCaseRequestDto;
import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@RestController
@RequestMapping(value = "/cases", produces = LI_WEB_API_JSON_V1)
@RequiredArgsConstructor
public class LegalCaseController implements BaseController {

	private final LegalCaseHandler handler;

	@Operation(summary = "Create basic legal case")
	@PostMapping("/basic")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Legal case created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('CLIENT_USER')")
	public LegalCaseDetailResponseDto createBasicCase(@Valid @RequestBody CreateBasicCaseRequestDto dto) {
		return handler.createBasicCase(dto);
	}

	@Operation(summary = "Create advanced legal case")
	@PostMapping("/advanced")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Legal case created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('CLIENT_USER')")
	public LegalCaseDetailResponseDto createBasicCase(@Valid @RequestBody CreateAdvancedCaseRequestDto dto) {
		return handler.createAdvancedCase(dto);
	}

	@Operation(summary = "Accept legal case")
	@PatchMapping("{caseId}/accept")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Legal case accepted successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('LAWYER_USER')")
	public LegalCaseResponseDto acceptCase(@PathVariable("caseId") UUID uuid) {
		return handler.acceptCase(uuid);
	}

	@Operation(summary = "Withdraw from case")
	@PatchMapping("{caseId}/withdraw")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Legal case withdrawn successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('LAWYER_USER')")
	public LegalCaseResponseDto withdrawCase(@PathVariable("caseId") UUID uuid) {
		return handler.withdrawCase(uuid);
	}

	@Operation(summary = "Get case detail")
	@PatchMapping("{caseId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved the case"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasAnyRole('LAWYER_USER', 'LAWYER_USER', 'CLIENT_USER')")
	public LegalCaseResponseDto getCaseById(@PathVariable("caseId") UUID uuid) {
		return handler.getCaseByUuid(uuid);
	}

	@Operation(summary = "Get case description")
	@PatchMapping("{caseId}/description")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved the case description"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasAnyRole('LAWYER_USER', 'CLIENT_USER')")
	public String getCaseDescriptionById(@PathVariable("caseId") UUID uuid) {
		return handler.getCaseDescriptionByUuid(uuid);
	}

	@Operation(summary = "Get page of legal cases")
	@GetMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved cases")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasAnyRole('LAWYER_USER', 'LAWYER_ADMIN', 'CLIENT_USER')")
	PageResponseDto<LegalCaseResponseDto> getCasesPage(@Valid @ParameterObject PageableRequestDto dto) {
		return handler.findCasesPage(dto);
	}

	@Operation(summary = "Get case files")
	@PatchMapping("{caseId}/files")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved case files"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasAnyRole('LAWYER_USER', 'CLIENT_USER')")
	public ListResponseDto<UUID> getCaseFilesById(@PathVariable("caseId") UUID uuid) {
		return handler.getCaseFilesByUuid(uuid);
	}

}
