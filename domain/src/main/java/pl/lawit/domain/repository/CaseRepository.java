package pl.lawit.domain.repository;

import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Case;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.kernel.repository.BaseRepository;

import java.util.UUID;

import static pl.lawit.domain.command.CaseCommand.CreateCase;
import static pl.lawit.domain.command.CaseCommand.UpdateCase;

public interface CaseRepository extends BaseRepository<Case> {

	Case create(CreateCase command);

	String getCaseDescriptionByCaseUuid(UUID uuid);

	PageResult<Case> findByUserUuid(UUID uuid, PageCommandQuery commandQuery);

	PageResult<Case> findByLawyerUuid(UUID uuid, PageCommandQuery commandQuery);

	PageResult<Case> findByCompanyUuid(UUID uuid, PageCommandQuery commandQuery);

	PageResult<Case> findPage(PageCommandQuery commandQuery);

	Case update(UpdateCase command);

	void deleteByUuid(UUID uuid);

}
