package pl.lawit.kernel.authentication;

import io.vavr.collection.Set;
import lombok.NonNull;
import pl.lawit.kernel.model.ApplicationUserRole;

import java.util.UUID;

import static pl.lawit.kernel.model.ApplicationUserRole.ADMIN_USER;
import static pl.lawit.kernel.model.ApplicationUserRole.CLIENT_USER;
import static pl.lawit.kernel.model.ApplicationUserRole.LAWYER_ADMIN;
import static pl.lawit.kernel.model.ApplicationUserRole.LAWYER_USER;

public record AuthenticatedUser(

	@NonNull
	String idpUid,

	@NonNull
	UUID userUuid,

	@NonNull
	Set<ApplicationUserRole> userRoles

) {

	public boolean isAdmin() {
		return userRoles.contains(ADMIN_USER);
	}

	public boolean isClient() {
		return userRoles.contains(CLIENT_USER);
	}

	public boolean isLawyer() {
		return userRoles.toJavaStream()
			.anyMatch(role -> role == LAWYER_ADMIN || role == LAWYER_USER);
	}

	@Override
	public String toString() {
		return "AuthenticatedUser{" +
			"uuid=" + userUuid +
			'}';
	}
}
