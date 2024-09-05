package pl.lawit.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import pl.lawit.application.configuration.SecurityProperties;
import pl.lawit.idp.firebase.configuration.FirebaseAuthenticationProvider;
import pl.lawit.kernel.model.ApplicationUserRole;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUri;

	private final JwtClaimResolver jwtClaimResolver;

	private final SecurityProperties securityProperties;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests($ ->
				$.requestMatchers(getPermittedMatchers()).permitAll()
					.anyRequest()
					.authenticated())
			.cors(withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.oauth2ResourceServer($ -> $.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
		FirebaseAuthenticationProvider provider = new FirebaseAuthenticationProvider(userDetailsService);
		return new ProviderManager(Collections.singletonList(provider));
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);

		OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(issuerUri);

		OAuth2TokenValidator<Jwt> delegate = new DelegatingOAuth2TokenValidator<>(issuerValidator);

		jwtDecoder.setJwtValidator(delegate);

		return jwtDecoder;
	}

	private String[] getPermittedMatchers() {
		return securityProperties.getPermittedMatchers().toArray(String[]::new);
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> jwtClaimResolver.getUserRoles(jwt)
			.map(this::mapToGrantedAuthority)
			.toJavaList());
		return jwtAuthenticationConverter;
	}

	private GrantedAuthority mapToGrantedAuthority(ApplicationUserRole userGroup) {
		return new SimpleGrantedAuthority("ROLE_" + userGroup.name());
	}
}