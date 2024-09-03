package pl.lawit.idp.firebase.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection for simplicity; adjust as needed
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").permitAll()  // Permit all requests to /api/users endpoints
                        .anyRequest().authenticated()  // Require authentication for all other requests
                )
        ;  // Use HTTP Basic Authentication; adjust as needed

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        FirebaseAuthenticationProvider provider = new FirebaseAuthenticationProvider(userDetailsService);
        return new ProviderManager(Collections.singletonList(provider));
    }
}