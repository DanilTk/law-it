package pl.lawit.domain.provider;

import com.google.firebase.auth.FirebaseAuthException;
import io.vavr.collection.Set;
import pl.lawit.domain.PageResultFireBase;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.kernel.model.EmailAddress;

public interface IdpProvider {

    ApplicationUser getBySub(String sub) throws FirebaseAuthException;

    ApplicationUser getByEmail(EmailAddress email) throws FirebaseAuthException;

    Set<ApplicationUserRole> getUserRoles(String sub) throws FirebaseAuthException;

    PageResultFireBase<ApplicationUser> findPage(String pageToken, int pageSize) throws FirebaseAuthException;

    ApplicationUser updateUserAccess(String sub, ApplicationUserRole role) throws FirebaseAuthException;

    void revokeUserAccess(String sub) throws FirebaseAuthException;

}
