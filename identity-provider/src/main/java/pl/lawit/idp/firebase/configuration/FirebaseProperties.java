package pl.lawit.idp.firebase.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.security.firebase")
public class FirebaseProperties {

	private String dbUrl;

	private String storageAccountKey;

}
