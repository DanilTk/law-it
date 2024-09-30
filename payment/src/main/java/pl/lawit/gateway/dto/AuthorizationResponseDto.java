package pl.lawit.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder(toBuilder = true)
public record AuthorizationResponseDto(

	@JsonProperty("access_token")
	String accessToken,

	@JsonProperty("token_type")
	String tokenType,

	@JsonProperty("expires_in")
	int expiresIn,

	@JsonProperty("client_credentials")
	String grantType

) {
}

