package pl.lawit.domain.repository;

import io.vavr.control.Option;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.model.EmailAddress;

import java.util.UUID;

public interface ApplicationUserRepository {

	ApplicationUser create(String idpSub, EmailAddress email);

	ApplicationUser getByUuid(UUID uuid);

	ApplicationUser getBySub(String sub);

	Option<ApplicationUser> findBySub(String sub);

	Option<ApplicationUser> findByEmail(EmailAddress email);

}
