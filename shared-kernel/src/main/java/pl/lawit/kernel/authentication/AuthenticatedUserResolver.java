package pl.lawit.kernel.authentication;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.kernel.provider.UserDetailsProvider;

import static pl.lawit.kernel.model.ApplicationUserRole.SYSTEM_USER;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserResolver {

	private final JwtClaimResolver jwtClaimResolver;

	private final UserDetailsProvider userDetailsProvider;

	public Option<AuthenticatedUser> findAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated() && authentication instanceof JwtAuthenticationToken auth) {
			AuthenticatedUser authenticatedUser = mapAuthenticatedUser(auth.getToken());
			return Option.of(authenticatedUser);
		}
		return Option.none();
	}

	public AuthenticatedUser getAuthenticatedUser() {
		return findAuthenticatedUser()
			.getOrElseThrow(() -> new AccessDeniedException("User is not authenticated."));
	}

	public AuthenticatedUser getSystemUser() {
		ApplicationUserDetails userDetails = userDetailsProvider.getSystemUser();
		return new AuthenticatedUser(userDetails.idpUid(), userDetails.uuid(), HashSet.of(SYSTEM_USER));
	}

	private AuthenticatedUser mapAuthenticatedUser(Jwt jwt) {
		ApplicationUserDetails userDetails;

		String idpUid = jwtClaimResolver.getUserIdpUid(jwt);
		Option<ApplicationUserDetails> userDetailsOption = userDetailsProvider.findByUid(idpUid);

		if (userDetailsOption.isEmpty()) {
			userDetails = userDetailsProvider.syncUserDetailsWithIdpByUid(idpUid);
		} else {
			userDetails = userDetailsOption.get();
		}

		Set<ApplicationUserRole> userRoles = HashSet.ofAll(jwtClaimResolver.getUserRoles(jwt));
		return new AuthenticatedUser(idpUid, userDetails.uuid(), userRoles);
	}

}
