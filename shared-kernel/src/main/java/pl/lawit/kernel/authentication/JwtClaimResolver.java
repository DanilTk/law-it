package pl.lawit.kernel.authentication;

import io.vavr.collection.Set;
import org.springframework.security.oauth2.jwt.Jwt;
import pl.lawit.kernel.model.ApplicationUserRole;

public interface JwtClaimResolver {

	Set<ApplicationUserRole> getUserRoles(Jwt jwt);

	String getUserIdpSub(Jwt jwt);

}
