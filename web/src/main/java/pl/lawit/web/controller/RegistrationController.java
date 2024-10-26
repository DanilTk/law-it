package pl.lawit.web.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.web.dto.RegistrationDto;
import pl.lawit.web.handler.RegistrationHandler;

import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/registration", produces = LI_WEB_API_JSON_V1)
public class RegistrationController implements BaseController {

	private final RegistrationHandler handler;

	@PostMapping
	ResponseEntity<String> addUserRole(@RequestBody RegistrationDto registrationDto) {
		ApplicationUserRole applicationUserRole;

		if("lawyer".equals(registrationDto.getRole())){
			applicationUserRole = ApplicationUserRole.LAWYER_ADMIN;
		} else{
			applicationUserRole = ApplicationUserRole.CLIENT_USER;
		}
		log.info("Registering User");
		try {

			handler.addUserRole(registrationDto.getFirebaseUserId(), applicationUserRole);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
		}



	}



}
