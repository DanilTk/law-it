package pl.lawit.kernel.authentication;

import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.model.ApplicationUserRole;

@Component
@RequiredArgsConstructor
public class UserRoleResolver {

	public Set<ApplicationUserRole> resolveUserRoles(List<String> roleNames) {
		return roleNames.flatMap(this::resolveRole)
			.toSet();
	}

	private Option<ApplicationUserRole> resolveRole(String groupName) {
		try {
			return Option.of(ApplicationUserRole.valueOf(groupName));
		} catch (IllegalArgumentException e) {
			return Option.none();
		}
	}

}
