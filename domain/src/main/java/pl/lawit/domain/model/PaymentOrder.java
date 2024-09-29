package pl.lawit.domain.model;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.model.MoneyAmount;
import pl.lawit.kernel.repository.BaseModel;

import java.net.URL;
import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record PaymentOrder(

	@NonNull
	UUID uuid,

	@NonNull
	String orderId,

	@NonNull
	PaymentStatus paymentStatus,

	@NonNull
	MoneyAmount amount,

	@NonNull
	CurrencyCode currencyCode,

	@NonNull
	PaymentType paymentType,

	@NonNull
	URL paymentLink,

	@NonNull
	UUID legalCaseUuid,

	@NonNull
	Instant createdAt,

	@NonNull
	Option<Instant> updatedAt,

	@NonNull
	UUID createdBy,

	@NonNull
	Option<UUID> updatedBy

) implements BaseModel {
}
