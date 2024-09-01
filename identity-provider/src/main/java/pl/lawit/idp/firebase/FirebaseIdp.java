package pl.lawit.idp.firebase;

import com.google.firebase.FirebaseApp;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.domain.provider.IdpProvider;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.PageResult;

@Component
@RequiredArgsConstructor
public class FirebaseIdp implements IdpProvider {

	private final FirebaseApp firebaseApp;

	@Override
	public ApplicationUser getBySub(String sub) {
		return null;
	}

	@Override
	public ApplicationUser getByEmail(EmailAddress email) {
		return null;
	}

	@Override
	public Set<ApplicationUserRole> getUserRoles(String sub) {
		return null;
	}

	@Override
	public Option<ApplicationUser> findByEmail(EmailAddress email) {
		return null;
	}

	@Override
	public PageResult<ApplicationUser> findPage(PageCommandQuery commandQuery) {
		return null;
	}

	@Override
	public ApplicationUser updateUserAccess(String sub, ApplicationUserRole role) {
		return null;
	}

	@Override
	public void revokeUserAccess(String sub) {

	}
}
