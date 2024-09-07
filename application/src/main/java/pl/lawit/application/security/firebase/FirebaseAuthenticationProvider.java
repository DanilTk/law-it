package pl.lawit.application.security.firebase;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof FirebaseAuthenticationToken)) {
            return null;
        }

        FirebaseAuthenticationToken firebaseToken = (FirebaseAuthenticationToken) authentication;

        Collection<GrantedAuthority> authorities = firebaseToken.getAuthorities();

        firebaseToken.setAuthenticated(true);
        return firebaseToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return FirebaseAuthenticationToken.class.isAssignableFrom(authentication);
    }
}