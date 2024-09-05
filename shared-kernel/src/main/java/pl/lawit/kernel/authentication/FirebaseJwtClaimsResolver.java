package pl.lawit.kernel.authentication;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.model.ApplicationUserRole;

@Component
@RequiredArgsConstructor
public class FirebaseJwtClaimsResolver implements JwtClaimResolver {

	@Value("${application.security.firebase.claims-namespace:null}")
	private String claimsNamespace;

	private static final String ROLES_CLAIM = "role";

	private final UserRoleResolver userRoleResolver;

	@Override
	public Set<ApplicationUserRole> getUserRoles(Jwt jwt) {
		return Option.of(jwt.getClaimAsStringList(claimsNamespace + "/" + ROLES_CLAIM))
			.map(List::ofAll)
			.map(userRoleResolver::resolveUserRoles)
			.getOrElse(HashSet::empty);
	}

	@Override
	public String getUserIdpSub(Jwt jwt) {
		return jwt.getSubject();
	}

}
