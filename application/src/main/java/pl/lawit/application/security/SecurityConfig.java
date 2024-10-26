package pl.lawit.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
import pl.lawit.kernel.authentication.JwtClaimResolver;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUri;

	private final JwtClaimResolver jwtClaimResolver;

	private final SecurityProperties securityProperties;

	private final CustomAuthenticationEntryPoint authenticationEntryPoint;

	private final CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.POST, "/api-web/**").permitAll()
				.requestMatchers(getPermittedMatchers()).permitAll()
				.anyRequest().authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
			.oauth2ResourceServer($ -> $.jwt(jwt -> {
				jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
				$.authenticationEntryPoint(authenticationEntryPoint);
			}))
			.exceptionHandling($ -> {
				$.accessDeniedHandler(accessDeniedHandler);
			});

		return http.build();
	}

	private String[] getPermittedMatchers() {
		return securityProperties.getPermittedMatchers().toArray(String[]::new);
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);
		OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(issuerUri);
		OAuth2TokenValidator<Jwt> delegate = new DelegatingOAuth2TokenValidator<>(issuerValidator);
		jwtDecoder.setJwtValidator(delegate);

		return jwtDecoder;
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtClaimResolver::getUserAuthorities);
		return jwtAuthenticationConverter;
	}

}


