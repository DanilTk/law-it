package pl.lawit.domain.command;

import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.authentication.AuthenticatedUser;

import java.util.UUID;

public interface PurchasedDocumentCommand {

	@Builder(toBuilder = true)
	record CreatePurchasedDocument(

		@NonNull
		UUID fileUuid,

		@NonNull
		UUID templateUuid,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

}
