package pl.lawit.domain.model;

import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record LegalCaseInfo(

	@NonNull
	LegalCase legalCase,

	@NonNull
	PaymentOrder paymentOrder

) {
}
