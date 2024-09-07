package pl.lawit.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.lawit.application.configuration.SecurityProperties;
import pl.lawit.application.security.firebase.FirebaseAuthenticationFilter;
import pl.lawit.application.security.firebase.FirebaseAuthenticationProvider;
import pl.lawit.kernel.exception.GlobalExceptionHandler;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
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
						.requestMatchers("/**").permitAll()
						.anyRequest().authenticated())
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
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