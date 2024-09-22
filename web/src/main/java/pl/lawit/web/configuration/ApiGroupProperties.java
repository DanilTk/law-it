package pl.lawit.web.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.api-groups")
public class ApiGroupProperties {

	private Set<String> templatesApiGroup;

	private Set<String> casesApiGroup;

	private Set<String> meetingsApiGroup;

	private Set<String> accountsApiGroup;

	private Set<String> filesApiGroup;

	private Set<String> paymentsApiGroup;

}
