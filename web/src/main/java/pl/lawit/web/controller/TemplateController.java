package pl.lawit.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lawit.domain.model.TemplateCategory;
import pl.lawit.kernel.model.FileDetail;
import pl.lawit.web.dto.ListResponseDto;
import pl.lawit.web.dto.TemplateDto.CreateTemplateRequestDto;
import pl.lawit.web.dto.TemplateDto.FindTemplatesRequestDto;
import pl.lawit.web.handler.TemplateHandler;

import java.net.URLEncoder;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static pl.lawit.web.configuration.OpenApiConfiguration.SECURITY_SCHEME_NAME;
import static pl.lawit.web.dto.TemplateDto.GenerateTemplatedDocumentRequestDto;
import static pl.lawit.web.dto.TemplateDto.TemplateResponseDto;
import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@RestController
@RequestMapping(value = "/templates", produces = LI_WEB_API_JSON_V1)
@RequiredArgsConstructor
public class TemplateController {

	private final TemplateHandler handler;

	@Operation(summary = "Create a template")
	@PostMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Template created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	public TemplateResponseDto createTemplate(@Valid @RequestBody CreateTemplateRequestDto dto) {
		return handler.createTemplate(dto);
	}

	@Operation(summary = "Generate templated document")
	@PostMapping("/{templateId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Document generated successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasAnyRole('ADMIN_USER','CLIENT_USER')")
	public ResponseEntity<ByteArrayResource> generateDocument(@PathVariable("templateId") UUID uuid,
															  @Valid @RequestBody GenerateTemplatedDocumentRequestDto dto) {
		FileDetail fileContent = handler.generateDocument(uuid, dto);
		String encodedFilename = URLEncoder.encode(fileContent.fileName().value(), UTF_8)
			.replace("+", "%20");

		return ResponseEntity.ok()
			.header(CONTENT_DISPOSITION, "attachment; filename=" + encodedFilename)
			.header(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION)
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(fileContent.fileContent());
	}

	@Operation(summary = "Find available templates")
	@GetMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved templates"),
		@ApiResponse(responseCode = "400", description = "Invalid input"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasAnyRole('CLIENT_USER', 'ADMIN_USER')")
	public ListResponseDto<TemplateResponseDto> findTemplates(@Valid @ParameterObject FindTemplatesRequestDto dto) {
		return handler.findTemplates(dto);
	}

	@Operation(summary = "Find template categories")
	@GetMapping("/categories")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved template categories"),
		@ApiResponse(responseCode = "401", description = "Unauthorized")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("isAuthenticated()")
	public ListResponseDto<TemplateCategory> findTemplateCategories() {
		return handler.findAllCategories();
	}

}
