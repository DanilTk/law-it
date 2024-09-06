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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String idToken = request.getHeader("Authorization");

        if (idToken == null || idToken.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Firebase ID-Token");
            return;
        }

        try {
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(idToken.replace("Bearer ", ""));

            String role = (String) token.getClaims().get("Role");
            List<GrantedAuthority> authorities = new ArrayList<>();

            if (role != null) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            FirebaseAuthenticationToken authenticationToken = new FirebaseAuthenticationToken(idToken, token, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase ID-Token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}