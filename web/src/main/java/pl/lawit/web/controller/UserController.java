package pl.lawit.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.control.Option;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lawit.web.dto.PageResponseDto;
import pl.lawit.web.dto.PageableRequestDto;
import pl.lawit.web.handler.UserHandler;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static pl.lawit.web.configuration.OpenApiConfiguration.SECURITY_SCHEME_NAME;
import static pl.lawit.web.dto.UserDto.UserResponseDto;
import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@RestController
@RequestMapping(value = "/users", produces = LI_WEB_API_JSON_V1)
@RequiredArgsConstructor
public class UserController implements BaseController {

	private final UserHandler handler;

	@Operation(summary = "Find all application users")
	@GetMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved the user"),
		@ApiResponse(responseCode = "404", description = "Object not found")

	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	PageResponseDto<UserResponseDto> findUsers(@Valid @ParameterObject PageableRequestDto dto) {
		return handler.findUsers(dto);
	}

	@Operation(summary = "Find user by email")
	@GetMapping("/{email}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved the user"),
		@ApiResponse(responseCode = "404", description = "Object not found")

	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	Option<UserResponseDto> findUserByEmail(@PathVariable @Valid @NotEmpty String email) {
		return handler.findByEmail(email);
	}

	@Operation(summary = "Trigger synchronization of users")
	@PutMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Users synchronized successfully")
	})
	@SecurityRequirement(name = SECURITY_SCHEME_NAME)
	@PreAuthorize("hasRole('ADMIN_USER')")
	@ResponseStatus(NO_CONTENT)
	void synchronizeUsers() {
		handler.synchronizeUsers();
	}

}
