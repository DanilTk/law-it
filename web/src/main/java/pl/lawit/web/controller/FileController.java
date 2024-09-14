package pl.lawit.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.lawit.web.dto.FileDto.FileResponseDto;
import pl.lawit.web.handler.FileHandler;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static pl.lawit.web.configuration.OpenApiConfiguration.SECURITY_SCHEME_NAME;
import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@RestController
@RequestMapping(value = "/files", produces = LI_WEB_API_JSON_V1)
@RequiredArgsConstructor
public class FileController implements BaseController{

	private final FileHandler handler;

	@Operation(summary = "Upload a file")
	@PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "File uploaded successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input"),
		@ApiResponse(responseCode = "429", description = "Too Many Requests")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("isAuthenticated()")
	FileResponseDto uploadFile(@RequestPart("file") MultipartFile file) {
		return handler.uploadFile(file);
	}

	@Operation(summary = "Get uploaded file details")
	@GetMapping("/{fileId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved the file"),
		@ApiResponse(responseCode = "404", description = "Object not found")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	FileResponseDto getFileById(@PathVariable("fileId") UUID uuid) {
		return handler.getFileByUuid(uuid);
	}

	@Operation(summary = "Delete uploaded file")
	@DeleteMapping("/{fileId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "File deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Object not found"),
		@ApiResponse(responseCode = "409", description = "Conflict: Object is in use")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	@ResponseStatus(NO_CONTENT)
	void deleteFile(@PathVariable("fileId") UUID uuid) {
		handler.deleteFile(uuid);
	}

}
