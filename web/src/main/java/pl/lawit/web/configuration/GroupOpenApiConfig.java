package pl.lawit.web.configuration;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GroupOpenApiConfig {

    private final ApiGroupProperties apiGroupProperties;

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


    @Bean
    public GroupedOpenApi adminGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("6_admin")
                .displayName("admin")
                .pathsToMatch(apiGroupProperties.getAdminApiGroup().toArray(new String[0]))
                .build();
    }
}
