package pl.lawit.kernel.authentication;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.kernel.provider.UserDetailsProvider;

import java.util.UUID;

import static pl.lawit.kernel.model.ApplicationUserRole.SYSTEM_USER;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserResolver {

	private final JwtClaimResolver jwtClaimResolver;

	private final UserDetailsProvider userDetailsProvider = null;

	public Option<AuthenticatedUser> findAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated() && authentication instanceof JwtAuthenticationToken auth) {
			AuthenticatedUser authenticatedUser = mapAuthenticatedUser(auth.getToken());
			return Option.of(authenticatedUser);
		}
		return Option.none();
	}

	public AuthenticatedUser getAuthenticatedUser() {
		return new AuthenticatedUser("system_idp", UUID.fromString("7f9c9b9a-4e5f-4d6c-8d7e-1a2b3c4d5e6f"),
			HashSet.of(ApplicationUserRole.ADMIN_USER));
//		return findAuthenticatedUser()
//			.getOrElseThrow(() -> new AccessDeniedException("User is not authenticated."));
	}

	public AuthenticatedUser getSystemUser() {
		ApplicationUserDetails userDetails = userDetailsProvider.getSystemUser();
		return new AuthenticatedUser(userDetails.sub(), userDetails.uuid(), HashSet.of(SYSTEM_USER));
	}

	private AuthenticatedUser mapAuthenticatedUser(Jwt jwt) {
		ApplicationUserDetails userDetails;

		String idpSub = jwtClaimResolver.getUserIdpSub(jwt);
		Option<ApplicationUserDetails> userDetailsOption = userDetailsProvider.findBySub(idpSub);

		if (userDetailsOption.isEmpty()) {
			userDetails = userDetailsProvider.syncUserDetailsWithIdpBySub(idpSub);
		} else {
			userDetails = userDetailsOption.get();
		}

		Set<ApplicationUserRole> userRoles = jwtClaimResolver.getUserRoles(jwt);
		return new AuthenticatedUser(idpSub, userDetails.uuid(), userRoles);
	}

}
