package pl.lawit.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.model.PaymentOrder;
import pl.lawit.kernel.authentication.AuthenticatedUser;

@Component
@RequiredArgsConstructor
public class CaseOrderResolver {

	private final PaymentOrderService paymentOrderService;

	private final LegalCaseService legalCaseService;

	@Transactional
	public void confirmOrder(String orderId, AuthenticatedUser systemUser) {
		PaymentOrder paymentOrder = paymentOrderService.getOrder(orderId);
		legalCaseService.submitLegalCase(paymentOrder.legalCaseUuid(), systemUser);
	}
}
