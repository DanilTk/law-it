package pl.lawit.domain.provider;

import io.vavr.collection.Set;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.TokenizedPageResult;

public interface IdpProvider {

	ApplicationUser getByUid(String uid);

	ApplicationUser getByEmail(EmailAddress email);

	Set<ApplicationUserRole> getUserRoles(String uid);

	TokenizedPageResult<ApplicationUser> findPage(String pageToken, int pageSize);

	ApplicationUser addUserRole(String uid, ApplicationUserRole role);

	void revokeUserAccess(String uid);

}
