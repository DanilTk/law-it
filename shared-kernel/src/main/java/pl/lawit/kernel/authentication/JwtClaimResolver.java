package pl.lawit.kernel.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import pl.lawit.kernel.model.ApplicationUserRole;

import java.util.Set;

public interface JwtClaimResolver {

	Set<GrantedAuthority> getUserAuthorities(Jwt jwt);

	Set<ApplicationUserRole> getUserRoles(Jwt jwt);

	String getUserIdpUid(Jwt jwt);

}
