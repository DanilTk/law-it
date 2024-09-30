package pl.lawit.domain.processor;

import pl.lawit.domain.model.LegalCase;
import pl.lawit.domain.model.PaymentResponse;
import pl.lawit.kernel.authentication.AuthenticatedUser;

public interface PaymentOrderProcessor {

	PaymentResponse placePaymentOrder(LegalCase legalCase,
									  AuthenticatedUser authenticatedUser, String ipAddress);

	String refundPayment();
}
