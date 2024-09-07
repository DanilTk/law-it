package pl.lawit.application.security;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import pl.lawit.kernel.model.ApplicationUserRole;

public interface JwtClaimResolver {

	Set<GrantedAuthority> getUserRoles(Jwt jwt);

	String getUserIdpSub(Jwt jwt);

}
