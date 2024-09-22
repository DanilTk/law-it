package pl.lawit.domain.mapper;

import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.authentication.ApplicationUserDetails;

@UtilityClass
public final class UserDetailsMapper {

	public static ApplicationUserDetails map(ApplicationUser applicationUser) {
		return ApplicationUserDetails.builder()
			.uuid(applicationUser.uuid().get())
			.idpUid(applicationUser.idpUid())
			.build();
	}
}
