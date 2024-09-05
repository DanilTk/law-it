package pl.lawit.application.security.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.List;

public class FirebaseAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public FirebaseAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = request.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7); // Remove "Bearer " prefix
			try {
				FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
				List<GrantedAuthority> authorities = getAuthoritiesFromToken(firebaseToken);

				FirebaseAuthenticationToken auth = new FirebaseAuthenticationToken(authorities);
				return auth;
			} catch (FirebaseAuthException e) {
				throw new RuntimeException("Invalid Firebase token", e);
			}
		}
		throw new RuntimeException("Authorization header is missing or invalid");
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											FilterChain chain, Authentication authResult) throws IOException,
		ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}

	private static List<GrantedAuthority> getAuthoritiesFromToken(FirebaseToken token) {
		Object claims = token.getClaims().get("authorities");
		List<String> permissions = (List<String>) claims;
		List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
		if (permissions != null && !permissions.isEmpty()) {
			authorities = AuthorityUtils.createAuthorityList(permissions);
		}
		return authorities;
	}
}