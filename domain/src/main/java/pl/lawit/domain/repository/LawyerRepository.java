package pl.lawit.domain.repository;

import io.vavr.collection.List;
import pl.lawit.domain.command.CompanyCommand.AddLawyerToCompany;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Lawyer;
import pl.lawit.domain.model.Pesel;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.kernel.repository.BaseRepository;

import java.util.UUID;

import static pl.lawit.domain.command.LawyerCommand.CreateLawyer;
import static pl.lawit.domain.command.LawyerCommand.UpdateLawyerCertificate;
import static pl.lawit.domain.command.LawyerCommand.UpdateLawyerRate;

public interface LawyerRepository extends BaseRepository<Lawyer> {

	Lawyer create(CreateLawyer command);

	PageResult<Lawyer> findPage(PageCommandQuery commandQuery);

	Lawyer getByUserUuid(UUID uuid);

	List<Lawyer> findLawyersByCompanyUuid(UUID uuid);

	boolean existsByCompanyUuid(UUID uuid);

	boolean existsByPesel(Pesel pesel);

	boolean existsByEmail(EmailAddress emailAddress);

	Lawyer update(UpdateLawyerRate command);

	Lawyer update(UpdateLawyerCertificate command);

	Lawyer assignToCompany(AddLawyerToCompany command);

	void unassignFromCompany(UUID uuid, AuthenticatedUser authenticatedUser);

}
