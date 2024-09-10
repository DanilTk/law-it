package pl.lawit.idp.firebase.mapper;

import com.google.firebase.auth.UserRecord;
import io.vavr.control.Option;
import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.util.DateTimeUtil;

import java.time.Instant;

@UtilityClass
public class ApplicationUserMapper {

	public ApplicationUser map(UserRecord userRecord) {
		Instant createdAt = DateTimeUtil.toInstant(userRecord.getUserMetadata().getCreationTimestamp());

		return ApplicationUser.builder()
			.uuid(Option.none())
			.idpSub(userRecord.getUid())
			.email(EmailAddress.of(userRecord.getEmail()))
			.isIdpUser(true)
			.idpSub(userRecord.getUid())
			.createdAt(createdAt)
			.build();
	}
}
