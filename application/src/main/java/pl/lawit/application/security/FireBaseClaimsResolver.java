package pl.lawit.application.security;

import io.vavr.collection.HashSet;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.authentication.JwtClaimResolver;
import pl.lawit.kernel.authentication.UserRoleResolver;
import pl.lawit.kernel.model.ApplicationUserRole;

import java.util.Collections;
import java.util.Set;

import static pl.lawit.kernel.authentication.ClaimKey.ROLE_CLAIM;

@Component
@RequiredArgsConstructor
public class FireBaseClaimsResolver implements JwtClaimResolver {

	private final UserRoleResolver userRoleResolver;

	@Override
	public Set<GrantedAuthority> getUserAuthorities(Jwt jwt) {
		return Option.of(jwt.getClaimAsStringList(ROLE_CLAIM.getKey()))
			.map(HashSet::ofAll)
			.map(userRoleResolver::resolveUserRoles)
			.map(roles -> roles.map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role.toString())))
			.map($ -> $.toJavaSet())
			.getOrElse(Collections::emptySet);
	}

	@Override
	public Set<ApplicationUserRole> getUserRoles(Jwt jwt) {
		return Option.of(jwt.getClaimAsStringList(ROLE_CLAIM.getKey()))
			.map(HashSet::ofAll)
			.map(userRoleResolver::resolveUserRoles)
			.map($ -> $.toJavaSet())
			.getOrElse(Collections::emptySet);
	}

	@Override
	public String getUserIdpUid(Jwt jwt) {
		return jwt.getSubject();
	}

}
