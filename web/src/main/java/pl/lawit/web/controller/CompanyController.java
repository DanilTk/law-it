package pl.lawit.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lawit.web.dto.CompanyDto.CompanyResponseDto;
import pl.lawit.web.dto.CompanyDto.UpdateCompanyRequestDto;
import pl.lawit.web.dto.ListResponseDto;
import pl.lawit.web.dto.PageResponseDto;
import pl.lawit.web.dto.PageableRequestDto;
import pl.lawit.web.handler.CompanyHandler;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static pl.lawit.web.configuration.OpenApiConfiguration.SECURITY_SCHEME_NAME;
import static pl.lawit.web.dto.CompanyDto.CreateCompanyRequestDto;
import static pl.lawit.web.dto.LawyerDto.LawyerResponseDto;
import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@RestController
@RequestMapping(value = "/companies", produces = LI_WEB_API_JSON_V1)
@RequiredArgsConstructor
public class CompanyController implements BaseController {

	private final CompanyHandler handler;

	@Operation(summary = "Create a new company profile")
	@PostMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Company profile created successfully")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	public CompanyResponseDto createCompany(@Valid @RequestBody CreateCompanyRequestDto dto) {
		return handler.createCompany(dto);
	}

	@Operation(summary = "Add a lawyer to a company")
	@PutMapping("/{companyId}/lawyers/{lawyerId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lawyer assigned to company successfully")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	public LawyerResponseDto addLawyerToCompany(@PathVariable("companyId") UUID companyUuid,
												@PathVariable("lawyerId") UUID lawyerUuid) {
		return handler.addCompanyLawyer(companyUuid, lawyerUuid);
	}

	@Operation(summary = "Get all lawyers belonging to a company")
	@GetMapping("/{companyId}/lawyers")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved company lawyers")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasAnyRole('ADMIN_USER', 'SUPPORT_USER', 'BASIC_USER')")
	public ListResponseDto<LawyerResponseDto> getCompanyLawyers(@PathVariable("companyId") UUID uuid) {
		return handler.getCompanyLawyers(uuid);
	}

	@Operation(summary = "Get page of company profiles")
	@GetMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved companies")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	public PageResponseDto<CompanyResponseDto> getCompaniesPage(@Valid @ParameterObject PageableRequestDto dto) {
		return handler.findCompaniesPage(dto);
	}

	@Operation(summary = "Get company profile")
	@GetMapping("/{companyId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved the company")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	public CompanyResponseDto getCompanyById(@PathVariable("companyId") UUID uuid) {
		return handler.getCompanyByUuid(uuid);
	}

	@Operation(summary = "Update existing company profile")
	@PatchMapping("/{companyId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Company updated successfully")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	public CompanyResponseDto updateCompany(@PathVariable("companyId") UUID uuid,
											@Valid @RequestBody UpdateCompanyRequestDto dto) {
		return handler.updateCompany(uuid, dto);
	}

	@Operation(summary = "Delete a company profile")
	@DeleteMapping("/{companyId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Company deleted successfully")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	@ResponseStatus(NO_CONTENT)
	public void deleteCompany(@PathVariable("companyId") UUID uuid) {
		handler.deleteCompany(uuid);
	}

	@Operation(summary = "Remove member from a company")
	@DeleteMapping("/{companyId}/lawyers/{lawyerId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Lawyer removed from company successfully")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	@ResponseStatus(NO_CONTENT)
	public void removeCompanyLawyer(@PathVariable("companyId") UUID companyUuid,
									@PathVariable("lawyerId") UUID lawyerUuid) {
		handler.removeCompanyLawyer(companyUuid, lawyerUuid);
	}
}
