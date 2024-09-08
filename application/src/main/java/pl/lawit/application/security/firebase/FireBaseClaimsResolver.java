package pl.lawit.application.security.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import pl.lawit.application.security.JwtClaimResolver;
import pl.lawit.idp.firebase.UserRoleResolver;
import pl.lawit.kernel.model.ApplicationUserRole;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class FireBaseClaimsResolver implements JwtClaimResolver {


    private static final String ROLES_CLAIM = "role";

    private final UserRoleResolver userRoleResolver;

    @Override
    public Set<GrantedAuthority> getUserRoles(Jwt jwt) {
        try {

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(jwt.getTokenValue());

            return resolveAuthorities(decodedToken);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    public String getUserIdpSub(Jwt jwt) {
        return jwt.getSubject();
    }

    private Set<GrantedAuthority> resolveAuthorities(FirebaseToken token) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        String role = (String) token.getClaims().get(ROLES_CLAIM);
        if (role != null) {
            ApplicationUserRole userRole = userRoleResolver.resolveUserRole(role.toUpperCase());
            if (Objects.nonNull(userRole)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            }
        }
        return authorities;
    }
}
