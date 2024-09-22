package pl.lawit.domain.repository;

import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.command.PurchasedDocumentCommand.CreatePurchasedDocument;
import pl.lawit.domain.model.PurchasedDocument;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.kernel.repository.BaseRepository;

public interface PurchasedDocumentRepository extends BaseRepository<PurchasedDocument> {

	PurchasedDocument create(CreatePurchasedDocument command);

	PageResult<PurchasedDocument> findAllUserDocuments(AuthenticatedUser authenticatedUser,
													   PageCommandQuery commandQuery);
}
