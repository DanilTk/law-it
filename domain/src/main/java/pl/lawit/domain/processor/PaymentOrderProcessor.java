package pl.lawit.domain.processor;

import pl.lawit.domain.model.PaymentOrderDetail;

public interface PaymentOrderProcessor {

	PaymentOrderDetail placePaymentOrder();

	String refundPayment();
}
