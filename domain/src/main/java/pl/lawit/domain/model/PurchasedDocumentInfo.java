package pl.lawit.domain.model;

import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record PurchasedDocumentInfo(

	@NonNull
	RegisteredFile purchasedDocumentFile,

	@NonNull
	PurchasedDocument purchasedDocument,

	@NonNull
	DocumentTemplate documentTemplate

) {
}
