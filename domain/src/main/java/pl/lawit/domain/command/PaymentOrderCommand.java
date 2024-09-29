package pl.lawit.domain.command;

import lombok.Builder;
import lombok.NonNull;
import pl.lawit.domain.model.CurrencyCode;
import pl.lawit.domain.model.PaymentStatus;
import pl.lawit.domain.model.PaymentType;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.MoneyAmount;

import java.net.URL;
import java.util.UUID;

public interface PaymentOrderCommand {

	@Builder(toBuilder = true)
	record CreatePaymentOrder(

		@NonNull
		UUID legalCaseUuid,

		@NonNull
		String orderId,

		@NonNull
		URL paymentLink,

		@NonNull
		CurrencyCode currencyCode,

		@NonNull
		MoneyAmount purchasePrice,

		@NonNull
		PaymentStatus paymentStatus,

		@NonNull
		PaymentType paymentType,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

}
