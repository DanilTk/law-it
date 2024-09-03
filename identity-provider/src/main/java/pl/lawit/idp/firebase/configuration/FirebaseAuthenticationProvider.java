package pl.lawit.idp.firebase.configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public FirebaseAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof String)) {
            throw new AuthenticationException("Principal is not a valid Firebase token") {};
        }
        String token = (String) principal;
        UserDetails user = userDetailsService.loadUserByUsername(token);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with token: " + token);
        }
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
