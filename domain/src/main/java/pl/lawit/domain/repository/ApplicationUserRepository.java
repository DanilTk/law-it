package pl.lawit.domain.repository;

import io.vavr.collection.List;
import io.vavr.control.Option;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.PageResult;

import java.util.UUID;

public interface ApplicationUserRepository {

	ApplicationUser create(String idpUid, EmailAddress email);

	List<ApplicationUser> findAll();

	PageResult<ApplicationUser> findAll(PageCommandQuery commandQuery);

	ApplicationUser getByUuid(UUID uuid);

	ApplicationUser getByIdpUid(String uid);

	Option<ApplicationUser> findByIdpUid(String uid);

	Option<ApplicationUser> findByEmail(EmailAddress email);

	ApplicationUser update(UUID uuid, EmailAddress email);

	void disableUsersInBatch(List<String> collection);

}
