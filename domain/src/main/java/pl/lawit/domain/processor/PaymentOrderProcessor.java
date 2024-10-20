package pl.lawit.domain.processor;

import pl.lawit.domain.command.PaymentOrderCommand.PlacePaymentOrder;
import pl.lawit.domain.model.PaymentResponseDto;

public interface PaymentOrderProcessor {

	PaymentResponseDto placePaymentOrder(PlacePaymentOrder command);

	String refundPayment();
}
