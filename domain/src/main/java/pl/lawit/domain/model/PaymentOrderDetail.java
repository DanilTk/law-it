package pl.lawit.domain.model;

import lombok.Builder;
import lombok.NonNull;

import java.net.URL;

@Builder(toBuilder = true)
public record PaymentOrderDetail(

	@NonNull
	String orderId,

	@NonNull
	URL paymentLink,

	@NonNull
	CurrencyCode currencyCode

) {
}
