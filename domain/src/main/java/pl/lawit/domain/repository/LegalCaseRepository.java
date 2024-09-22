package pl.lawit.domain.repository;

import io.vavr.collection.Set;
import pl.lawit.domain.command.CaseCommand.AcceptLegalCase;
import pl.lawit.domain.command.CaseCommand.CreateFileAttachments;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.LegalCase;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.kernel.repository.BaseRepository;

import java.util.UUID;

import static pl.lawit.domain.command.CaseCommand.CreateCase;
import static pl.lawit.domain.command.CaseCommand.CreateCaseHistory;
import static pl.lawit.domain.command.CaseCommand.SubmitLegalCase;
import static pl.lawit.domain.command.CaseCommand.UpdateCase;

public interface LegalCaseRepository extends BaseRepository<LegalCase> {

	LegalCase create(CreateCase command);

	Set<UUID> saveCaseAttachments(CreateFileAttachments command);

	Set<UUID> getCaseAttachments(UUID uuid);

	void createCaseHistory(CreateCaseHistory command);

	String getCaseDescriptionByCaseUuid(UUID uuid);

	PageResult<LegalCase> findByUserUuid(UUID uuid, PageCommandQuery commandQuery);

	PageResult<LegalCase> findLawyerCases(UUID uuid, PageCommandQuery commandQuery);

	PageResult<LegalCase> findByCompanyUuid(UUID uuid, PageCommandQuery commandQuery);

	PageResult<LegalCase> findPage(PageCommandQuery commandQuery);

	LegalCase update(UpdateCase command);

	LegalCase submit(SubmitLegalCase command);

	LegalCase accept(AcceptLegalCase command);

	LegalCase withdraw(UUID uuid, AuthenticatedUser authenticatedUser);

	void deleteByUuid(UUID uuid);
}
