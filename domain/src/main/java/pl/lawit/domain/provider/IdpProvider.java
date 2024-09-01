package pl.lawit.domain.provider;

import io.vavr.collection.Set;
import io.vavr.control.Option;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.PageResult;

public interface IdpProvider {

	ApplicationUser getBySub(String sub);

	ApplicationUser getByEmail(EmailAddress email);

	Set<ApplicationUserRole> getUserRoles(String sub);

	Option<ApplicationUser> findByEmail(EmailAddress email);

	PageResult<ApplicationUser> findPage(PageCommandQuery commandQuery);

	ApplicationUser updateUserAccess(String sub, ApplicationUserRole role);

	void revokeUserAccess(String sub);

}
