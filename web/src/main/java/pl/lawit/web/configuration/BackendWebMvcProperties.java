package pl.lawit.web.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.web-mvc")
@Data
public class BackendWebMvcProperties {

	private String apiPrefix;

}
