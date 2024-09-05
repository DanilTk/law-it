package pl.lawit.kernel.provider;

import io.vavr.control.Option;
import pl.lawit.kernel.authentication.ApplicationUserDetails;
import pl.lawit.kernel.model.EmailAddress;

public interface UserDetailsProvider {

	Option<ApplicationUserDetails> findBySub(String sub);

	ApplicationUserDetails syncUserDetailsWithIdpBySub(String idpSub);

	ApplicationUserDetails syncUserWithIdpByEmail(EmailAddress email);

	ApplicationUserDetails getSystemUser();

}
