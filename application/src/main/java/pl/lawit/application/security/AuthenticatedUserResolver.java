package pl.lawit.application.security;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.authentication.AuthenticatedUser;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserResolver {

	private final JwtClaimResolver jwtClaimResolver;

//	private final UserDetailsProvider userDetailsProvider;

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
//		ApplicationUserDetails userDetails = userDetailsProvider.getSystemUser();
//		return new AuthenticatedUser(userDetails.sub(), userDetails.uuid(), HashSet.of(SYSTEM_USER));
		return null;
	}

	private AuthenticatedUser mapAuthenticatedUser(Jwt jwt) {
//		ApplicationUserDetails userDetails;
//
//		String idpSub = jwtClaimResolver.getUserIdpSub(jwt);
//		Option<ApplicationUserDetails> userDetailsOption = userDetailsProvider.findBySub(idpSub);
//
//		if (userDetailsOption.isEmpty()) {
//			userDetails = userDetailsProvider.syncUserDetailsWithIdpBySub(idpSub);
//		} else {
//			userDetails = userDetailsOption.get();
//		}
//
//		Set<UserRole> userRoles = jwtClaimResolver.getUserRoles(jwt);
//		return new AuthenticatedUser(idpSub, userDetails.uuid(), userRoles);
		return null;
	}

}
