package pl.lawit.web.mapper;

import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.PaymentOrder;
import pl.lawit.web.dto.PaymentOrderDto.PaymentOrderResponseDto;

@UtilityClass
public class PaymentDtoMapper {

	public static PaymentOrderResponseDto map(PaymentOrder paymentOrder) {
		return PaymentOrderResponseDto.builder()
			.paymentLink(paymentOrder.paymentLink())
			.orderId(paymentOrder.orderId())
			.build();
	}
}
