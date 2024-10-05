package pl.lawit.gateway.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Data
@Configuration
@ConfigurationProperties("payment-gateway.payu")
public class PaymentGatewayPayUProperties {

	private URL baseUrl;

	private URL authorizationUrl;

	private URL callbackUrl;

	private String clientId;

	private String clientSecret;

	private String callbackIp;

	private String grantType;

}
