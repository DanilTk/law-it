package pl.lawit.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.lawit.application.configuration.SecurityProperties;
import pl.lawit.application.security.firebase.FirebaseAuthenticationFilter;
import pl.lawit.application.security.firebase.FirebaseAuthenticationProvider;
import pl.lawit.kernel.model.ApplicationUserRole;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


	private final SecurityProperties securityProperties;
	private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(firebaseAuthenticationFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(getPermittedMatchers()).permitAll()
                        .anyRequest().authenticated())
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
                //.oauth2ResourceServer($ -> $.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

		return http.build();
	}



	private String[] getPermittedMatchers() {
		return securityProperties.getPermittedMatchers().toArray(String[]::new);
	}

	@Bean
	public FirebaseAuthenticationProvider firebaseAuthenticationProvider() {
		return new FirebaseAuthenticationProvider();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Collections.singletonList(firebaseAuthenticationProvider()));
	}


}