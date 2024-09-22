package pl.lawit.domain.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.model.LegalCase;
import pl.lawit.domain.model.PaymentOrder;
import pl.lawit.domain.model.PaymentOrderDetail;
import pl.lawit.domain.processor.PaymentOrderProcessor;
import pl.lawit.domain.repository.PaymentOrderRepository;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.MoneyAmount;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import static pl.lawit.domain.command.PaymentOrderCommand.CreatePaymentOrder;
import static pl.lawit.domain.model.CaseCategory.BASIC;
import static pl.lawit.domain.model.PaymentStatus.PENDING;
import static pl.lawit.domain.model.PaymentType.PURCHASE;

@Service
@RequiredArgsConstructor
public class PaymentOrderService {

	private final PaymentOrderPriceProperties paymentOrderPriceProperties;

	private final PaymentOrderRepository paymentOrderRepository;

	private final PaymentOrderProcessor paymentOrderProcessor;

	@Transactional
	public PaymentOrder getOrder(String orderId) {
		return paymentOrderRepository.getByOrderId(orderId);
	}

	@Transactional(propagation = MANDATORY)
	public PaymentOrder createOrder(LegalCase legalCase, AuthenticatedUser authenticatedUser) {
		PaymentOrderDetail orderDetail = paymentOrderProcessor.placePaymentOrder();
		MoneyAmount price = resolvePrice(legalCase);

		CreatePaymentOrder command = CreatePaymentOrder.builder()
			.legalCaseUuid(legalCase.uuid())
			.paymentStatus(PENDING)
			.paymentType(PURCHASE)
			.orderId(orderDetail.orderId())
			.paymentLink(orderDetail.paymentLink())
			.currencyCode(orderDetail.currencyCode())
			.purchasePrice(price)
			.authenticatedUser(authenticatedUser)
			.build();

		return paymentOrderRepository.create(command);
	}

	@Transactional(propagation = MANDATORY)
	public void refundOrder(UUID legalCaseUuid) {
		String orderId = paymentOrderProcessor.refundPayment();
	}

	private MoneyAmount resolvePrice(LegalCase legalCase) {
		if (legalCase.caseCategory() == BASIC) {
			return MoneyAmount.of(paymentOrderPriceProperties.getBasicCasePrice());
		} else {
			return MoneyAmount.of(paymentOrderPriceProperties.getAdvancedCasePrice());
		}
	}
}

@Getter
@Setter
@Configuration
@ConfigurationProperties("application.legal-case-pricing")
class PaymentOrderPriceProperties {

	private BigDecimal basicCasePrice;

	private BigDecimal advancedCasePrice;

}