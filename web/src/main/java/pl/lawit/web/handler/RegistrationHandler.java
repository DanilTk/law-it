package pl.lawit.web.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.idp.firebase.FirebaseIdp;
import pl.lawit.kernel.model.ApplicationUserRole;

@Component
@RequiredArgsConstructor
public class RegistrationHandler {

	private final FirebaseIdp firebaseIdp;


	public ApplicationUser addUserRole(String uid, ApplicationUserRole role) {

		return firebaseIdp.addUserRole(uid, role);
	}
}
