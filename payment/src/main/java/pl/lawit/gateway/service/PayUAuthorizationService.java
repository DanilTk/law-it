package pl.lawit.gateway.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import pl.lawit.gateway.configuration.PaymentGatewayPayuConfig;
import pl.lawit.gateway.dto.AuthorizationResponseDto;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class PayUAuthorizationService {

	@Value("${payment-gateway.payu.clientId}")
	private String clientId;

	@Value("${payment-gateway.payu.clientSecret}")
	private String clientSecret;

	private final RestClient restClient;

	private final AtomicReference<String> bearerToken = new AtomicReference<>("");

	private final AtomicReference<Long> tokenExpiryTime = new AtomicReference<>(0L);

	private final PaymentGatewayPayuConfig paymentGatewayPayuConfig;

//	TODO: Use spring cache and add token there. Add cache eviction after token expiry. If token is not expired, return it from cache, otherwise generate new token.
	public void generateAccessToken() {
		AuthorizationResponseDto authorizationResponseDto;
		ResponseEntity<AuthorizationResponseDto> responseEntity;

		if (isTokenExpired()) {
			URI uri = URI.create(paymentGatewayPayuConfig.getAuthenticationUrl());
			MultiValueMap<String, String> formData = getAuthenticationParams();

			try {
				responseEntity = restClient
					.post()
					.uri(uri)
					.header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
					.body(formData)
					.retrieve()
					.toEntity(AuthorizationResponseDto.class);

				if (responseEntity.getStatusCode() != OK) {
					// todo: define custom exception and throw it. E.g. PaymentProcessingException
				}

				authorizationResponseDto = responseEntity.getBody();
			} catch (Exception e) {
				// todo: define custom exception and throw it. E.g. PaymentProcessingException
				System.out.println("Error during authentication: " + e.getStackTrace());
				throw new RuntimeException("Authentication failed", e);
			}

			bearerToken.set(authorizationResponseDto.getAccessToken());
			tokenExpiryTime.set(System.currentTimeMillis() + authorizationResponseDto.getExpiresIn() * 1000L);
		}
	}

    private MultiValueMap<String, String> getAuthenticationParams() {
//		todo: USE paymentGatewayPayuConfig and convert it to map with ObjectMapepr
		return new LinkedMultiValueMap<>() {{
			add("grant_type", "client_credentials");//move grant type to payu config
			add("client_id", paymentGatewayPayuConfig.getClientId());
			add("client_secret", paymentGatewayPayuConfig.getClientSecret());
		}};
	}

	private boolean isTokenExpired() {
		return System.currentTimeMillis() >= tokenExpiryTime.get();
	}

}
