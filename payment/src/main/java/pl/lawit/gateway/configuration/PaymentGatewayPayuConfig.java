package pl.lawit.gateway.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "payment-gateway.payu")
public class PaymentGatewayPayuConfig {

    private String baseUrl;

    private String authenticationUrl;

    private String clientId;

    private String clientSecret;
}
