package pl.lawit.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lawit.web.dto.RegistrationDto;
import pl.lawit.web.handler.RegistrationHandler;

import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/registration", produces = LI_WEB_API_JSON_V1)
public class RegistrationController implements BaseController {

	private final RegistrationHandler handler;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN_USER')")
	ResponseEntity<String> addUserRole(@RequestBody RegistrationDto registrationDto) {

		try {
			handler.addUserRole(registrationDto.getUid(), registrationDto.getRole());
			return ResponseEntity.ok("User registered successfully.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
		}



	}



}
