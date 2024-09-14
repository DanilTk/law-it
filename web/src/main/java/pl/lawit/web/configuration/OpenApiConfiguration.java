package pl.lawit.web.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {

	public static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

	@Bean
	public OpenAPI openApi() {
		SecurityScheme securityScheme = new SecurityScheme()
			.type(HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.description("Firebase JWT Token authentication");

		return new OpenAPI()
			.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
			.components(new Components()
				.addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme));
	}

}
