package pl.lawit.web.dto;

import lombok.Getter;
import pl.lawit.kernel.model.ApplicationUserRole;

@Getter
public class RegistrationDto {

	String uid;
	ApplicationUserRole role;
}
