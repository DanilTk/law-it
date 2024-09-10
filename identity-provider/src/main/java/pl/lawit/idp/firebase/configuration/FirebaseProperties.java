package pl.lawit.idp.firebase.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.security.firebase")
@Getter
@Setter
public class FirebaseProperties {

    private String dbUrl;

    private String storageAccountKey;

}
