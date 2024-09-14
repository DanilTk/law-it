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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lawit.web.dto.PageResponseDto;
import pl.lawit.web.dto.PageableRequestDto;
import pl.lawit.web.handler.LawyerHandler;

import java.util.UUID;

import static pl.lawit.web.configuration.OpenApiConfiguration.SECURITY_SCHEME_NAME;
import static pl.lawit.web.dto.LawyerDto.CreateLawyerRequestDto;
import static pl.lawit.web.dto.LawyerDto.LawyerResponseDto;
import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@RestController
@RequestMapping(value = "/lawyers", produces = LI_WEB_API_JSON_V1)
@RequiredArgsConstructor
public class LawyerController implements BaseController {

	private final LawyerHandler handler;

	@Operation(summary = "Create a new company profile")
	@PostMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Lawyer profile created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	LawyerResponseDto createCompany(@Valid @RequestBody CreateLawyerRequestDto dto) {
		return handler.createLawyer(dto);
	}

	@Operation(summary = "Get lawyer details")
	@GetMapping("/{lawyerId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved the file"),
		@ApiResponse(responseCode = "404", description = "Object not found")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	LawyerResponseDto getFileById(@PathVariable("lawyerId") UUID uuid) {
		return handler.getLawyerByUuid(uuid);
	}

	@Operation(summary = "Get page of lawyer profiles")
	@GetMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved lawyers")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	PageResponseDto<LawyerResponseDto> getLawyersPage(@Valid @ParameterObject PageableRequestDto dto) {
		return handler.findLawyersPage(dto);
	}

}
