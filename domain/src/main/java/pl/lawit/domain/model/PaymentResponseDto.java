package pl.lawit.domain.model;

import lombok.NonNull;

public record PaymentResponseDto(

	@NonNull
	String redirectUri,

	@NonNull
	String orderId

) {

}
