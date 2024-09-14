package pl.lawit.kernel.provider;

import io.vavr.control.Option;
import pl.lawit.kernel.authentication.ApplicationUserDetails;

public interface UserDetailsProvider {

	Option<ApplicationUserDetails> findByUid(String uib);

	ApplicationUserDetails syncUserDetailsWithIdpByUid(String uid);

	ApplicationUserDetails getSystemUser();

}
