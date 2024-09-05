package pl.lawit.web.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.OAUTH2;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {

	public static final String SECURITY_OAUTH2 = "OAuth2";

	private final ApplicationProperties applicationProperties;

	private final ApiGroupProperties apiGroupProperties;

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String resourceServerUrl;

	@Bean
	public OpenAPI openApi() {
		Server server = new Server().url(applicationProperties.getServerUrl());
		Components components = new Components()
			.addSecuritySchemes(SECURITY_OAUTH2, getAuthorizationCodeFlowSecurityScheme());

		return new OpenAPI()
			.info(createServerInfo())
			.addServersItem(server)
			.components(components);
	}

	@Bean
	public GroupedOpenApi casesGroupedOpenApi() {
		return GroupedOpenApi.builder()
			.group("1_cases")
			.displayName("cases")
			.pathsToMatch(apiGroupProperties.getCasesApiGroup().toArray(new String[0]))
			.build();
	}

	@Bean
	public GroupedOpenApi templatesGroupedOpenApi() {
		return GroupedOpenApi.builder()
			.group("2_templates")
			.displayName("templates")
			.pathsToMatch(apiGroupProperties.getTemplatesApiGroup().toArray(new String[0]))
			.build();
	}

	@Bean
	public GroupedOpenApi conferencesGroupedOpenApi() {
		return GroupedOpenApi.builder()
			.group("3_meetings")
			.displayName("meetings")
			.pathsToMatch(apiGroupProperties.getMeetingsApiGroup().toArray(new String[0]))
			.build();
	}

	@Bean
	public GroupedOpenApi accountGroupedOpenApi() {
		return GroupedOpenApi.builder()
			.group("4_accounts")
			.displayName("accounts")
			.pathsToMatch(apiGroupProperties.getAccountsApiGroup().toArray(new String[0]))
			.build();
	}

	@Bean
	public GroupedOpenApi fileGroupedOpenApi() {
		return GroupedOpenApi.builder()
			.group("5_files")
			.displayName("files")
			.pathsToMatch(apiGroupProperties.getFilesApiGroup().toArray(new String[0]))
			.build();
	}

	private Info createServerInfo() {
		return new Info().title("Portfolio manager API").version(applicationProperties.getVersion());
	}

	private SecurityScheme getAuthorizationCodeFlowSecurityScheme() {
		return new SecurityScheme()
			.type(OAUTH2)
			.flows(getAuthorizationCodeFlow());
	}

	private OAuthFlows getAuthorizationCodeFlow() {
		OAuthFlow oAuthFlow = new OAuthFlow()
			.authorizationUrl(resourceServerUrl + "authorize")
			.tokenUrl(resourceServerUrl + "oauth/token");

		return new OAuthFlows().authorizationCode(oAuthFlow);
	}

}
