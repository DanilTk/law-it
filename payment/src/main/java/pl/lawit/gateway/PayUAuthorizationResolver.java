package pl.lawit.gateway;

import io.vavr.control.Option;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import pl.lawit.gateway.configuration.PaymentGatewayPayUProperties;
import pl.lawit.gateway.dto.AuthorizationResponseDto;
import pl.lawit.kernel.exception.PaymentProviderException;

import java.time.Duration;
import java.time.Instant;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static pl.lawit.kernel.logger.ApplicationLoggerFactory.paymentGatewayLogger;

@Getter
@Service
@RequiredArgsConstructor
public class PayUAuthorizationResolver {

	private static final String TOKEN_CACHE_NAME = "payment-gateway-token-cache";

	private static final String TOKEN_CACHE_KEY = "token";

	private final CacheManager cacheManager;

	private final RestClient restClient;

	private final PaymentGatewayPayUProperties paymentGatewayProperties;

	String getAccessToken() {
		return retrieveFromCache()
			.getOrElse(generateAndCache());
	}

	private String generateAndCache() {
		AuthorizationResponseDto tokenDetails = generateAccessToken();
		Instant expiryTime = Instant.ofEpochMilli(tokenDetails.expiresIn());
		cacheWithExpiry(TOKEN_CACHE_KEY, tokenDetails.accessToken(), expiryTime);
		return tokenDetails.accessToken();
	}

	private void cacheWithExpiry(String key, String token, Instant expiryTime) {
		Cache cache = cacheManager.getCache(TOKEN_CACHE_NAME);

		@SuppressWarnings("unchecked")
		com.github.benmanes.caffeine.cache.Cache<String, String> nativeCache =
			(com.github.benmanes.caffeine.cache.Cache<String, String>) cache.getNativeCache();

		long ttl = Duration.between(Instant.now(), expiryTime).getSeconds();

		nativeCache.put(key, token);
		nativeCache.policy().expireAfterWrite().ifPresent(expiry -> expiry.setExpiresAfter(Duration.ofSeconds(ttl)));

	}

	private Option<String> retrieveFromCache() {
		Cache cache = cacheManager.getCache(TOKEN_CACHE_NAME);
		Cache.ValueWrapper valueWrapper = cache.get(TOKEN_CACHE_KEY);


		return valueWrapper != null ? Option.of((String) valueWrapper.get()) : Option.none();
	}

	@SneakyThrows
	private AuthorizationResponseDto generateAccessToken() {
		MultiValueMap<String, String> authenticationProperties = getAuthenticationProperties();

		ResponseEntity<AuthorizationResponseDto> responseEntity = restClient
			.post()
			.uri(paymentGatewayProperties.getAuthorizationUrl().toURI())
			.header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
			.body(authenticationProperties)
			.retrieve()
			.toEntity(AuthorizationResponseDto.class);

		if (responseEntity.getStatusCode() != OK) {
			paymentGatewayLogger().error("Error while processing payment. Status code: {}",
				responseEntity.getStatusCode());
			throw new PaymentProviderException("Problem encountered processing your payment.");
		} else if (responseEntity.getBody() == null) {
			paymentGatewayLogger().error("Error while processing payment. Response body is null.");
			throw new PaymentProviderException("Something went wrong while processing your payment.");
		}

		return responseEntity.getBody();
	}

	private MultiValueMap<String, String> getAuthenticationProperties() {
		MultiValueMap<String, String> properties = new LinkedMultiValueMap<>();
		properties.add("grant_type", paymentGatewayProperties.getGrantType());
		properties.add("client_id", paymentGatewayProperties.getClientId());
		properties.add("client_secret", paymentGatewayProperties.getClientSecret());
		return properties;
	}

}
