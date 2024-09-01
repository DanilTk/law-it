package pl.lawit.application.configuration;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
//@ConfigurationProperties("application.security")
public class SecurityProperties {

	private List<String> permittedMatchers;
}