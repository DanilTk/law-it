package pl.lawit.web.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.web-mvc")
public class BackendWebMvcProperties {

	private String apiWebPrefix;

	private String apiPrefix;

}
