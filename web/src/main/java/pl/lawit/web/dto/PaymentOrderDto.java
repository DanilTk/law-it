package pl.lawit.web.dto;

import lombok.Builder;
import lombok.NonNull;

import java.net.URL;

public interface PaymentOrderDto {

	@Builder(toBuilder = true)
	record PaymentOrderResponseDto(

		@NonNull
		URL paymentLink,

		@NonNull
		String orderId

	) {

	}

}
