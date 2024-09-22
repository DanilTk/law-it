package pl.lawit.web.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.service.CaseOrderResolver;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;

@Component
@RequiredArgsConstructor
public class PaymentHandler {

	private final CaseOrderResolver caseOrderResolver;

	private final AuthenticatedUserResolver authenticatedUserResolver;

	public void process(String orderId) {
		AuthenticatedUser systemUser = authenticatedUserResolver.getSystemUser();
		caseOrderResolver.confirmOrder(orderId, systemUser);
	}
}
