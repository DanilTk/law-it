package pl.lawit.web.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
				.components(new Components()
						.addSecuritySchemes("bearerAuth", new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
								.description("Firebase JWT Token authentication")
						)
				).addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement()
						.addList("bearerAuth"));
	}
}
