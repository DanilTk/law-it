package pl.lawit.kernel.authentication;

import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.model.ApplicationUserRole;

import java.util.Map;

import static pl.lawit.kernel.authentication.ClaimKey.ROLE_CLAIM;

@Component
@RequiredArgsConstructor
public class UserRoleResolver {

	public ApplicationUserRole resolveUserRole(String roleName) {
		return resolveRole(roleName).getOrNull();
	}

	public Set<ApplicationUserRole> resolveUserRoles(Map<String, Object> claims) {
		return List.ofAll((java.util.List<String>) claims.get(ROLE_CLAIM.getKey()))
			.flatMap(this::resolveRole)
			.toSet();
	}

	public Set<ApplicationUserRole> resolveUserRoles(Set<String> roleNames) {
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
