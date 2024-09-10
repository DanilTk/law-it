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

	@Bean
	public OpenAPI openApi() {
		SecurityScheme securityScheme = new SecurityScheme()
			.type(HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.description("Firebase JWT Token authentication");

		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes("bearerAuth", securityScheme))
			.addSecurityItem(new SecurityRequirement()
				.addList("bearerAuth"));
	}

}
