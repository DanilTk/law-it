package pl.lawit.domain.model;

import lombok.NonNull;

public record PaymentResponse(

	@NonNull
	String redirectUri,

	@NonNull
	String orderId

) {

}
