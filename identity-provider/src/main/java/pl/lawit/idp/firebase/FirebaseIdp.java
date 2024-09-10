package pl.lawit.idp.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.domain.provider.IdpProvider;
import pl.lawit.idp.firebase.mapper.ApplicationUserMapper;
import pl.lawit.kernel.authentication.UserRoleResolver;
import pl.lawit.kernel.exception.IdpProviderException;
import pl.lawit.kernel.exception.ObjectNotFoundException;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.TokenizedPageResult;

import java.util.HashMap;
import java.util.Map;

import static pl.lawit.kernel.authentication.ClaimKey.ROLE_CLAIM;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirebaseIdp implements IdpProvider {

	private final FirebaseAuth firebaseAuth;

	private final UserRoleResolver userRoleResolver;

	@Override
	public ApplicationUser getByUid(String uid) {
		UserRecord userRecord = getUserRecordByUid(uid);
		return ApplicationUserMapper.map(userRecord);
	}

	@Override
	public ApplicationUser getByEmail(EmailAddress email) {
		UserRecord userRecord;

		try {
			userRecord = firebaseAuth.getUserByEmail(email.value());
		} catch (FirebaseAuthException e) {
			log.error("Error fetching user for email '{}': {}", email.value(), e.getMessage(), e);
			throw ObjectNotFoundException.byEmail(email.value(), ApplicationUser.class);
		}

		return ApplicationUserMapper.map(userRecord);
	}

	@Override
	public TokenizedPageResult<ApplicationUser> findPage(String pageToken, int pageSize) {
		ListUsersPage page;

		try {
			page = firebaseAuth.listUsers(pageToken, pageSize);
		} catch (FirebaseAuthException e) {
			return TokenizedPageResult.<ApplicationUser>builder()
				.content(List.empty())
				.nextPageToken(Option.none())
				.build();
		}

		List<UserRecord> userRecordList = List.ofAll(page.getValues());

		List<ApplicationUser> applicationUserList = userRecordList.map(ApplicationUserMapper::map);

		return TokenizedPageResult.<ApplicationUser>builder()
			.content(applicationUserList)
			.nextPageToken(Option.of(page.getNextPageToken()))
			.build();
	}

	@Override
	public Set<ApplicationUserRole> getUserRoles(String uid) {
		UserRecord userRecord;

		try {
			userRecord = firebaseAuth.getUser(uid);
		} catch (FirebaseAuthException e) {
			return HashSet.empty();
		}

		return userRoleResolver.resolveUserRoles(userRecord.getCustomClaims());
	}

	@Override
	public ApplicationUser addUserRole(String uid, ApplicationUserRole role) {
		UserRecord userRecord = getUserRecordByUid(uid);

		Map<String, Object> currentCustomClaims = userRecord.getCustomClaims();
		Map<String, Object> updatedClaims = setUserClaim(currentCustomClaims, role.name());

		try {
			FirebaseAuth.getInstance().setCustomUserClaims(uid, updatedClaims);
		} catch (FirebaseAuthException e) {
			throw new IdpProviderException(String.format("Error adding role for user: %s", uid));
		}

		return ApplicationUserMapper.map(userRecord);
	}

	@Override
	public void revokeUserAccess(String uid) {
		UserRecord userRecord = getUserRecordByUid(uid);

		Map<String, Object> currentCustomClaims = userRecord.getCustomClaims();

		Map<String, Object> updatedClaims = new HashMap<>(currentCustomClaims);
		updatedClaims.remove(ROLE_CLAIM.getKey());

		try {
			FirebaseAuth.getInstance().setCustomUserClaims(uid, updatedClaims);
		} catch (FirebaseAuthException e) {
			throw new IdpProviderException(String.format("Error removing role for user: %s", uid));
		}

	}

	private Map<String, Object> setUserClaim(Map<String, Object> customClaims, String value) {
		Map<String, Object> mutableClaims = new HashMap<>();

		List<String> claimValues = List.of(value);

		for (Map.Entry<String, Object> entry : customClaims.entrySet()) {
			if (entry.getKey().equals(ROLE_CLAIM.getKey())) {
				mutableClaims.put(entry.getKey(), claimValues);
			} else {
				mutableClaims.put(entry.getKey(), entry.getValue());
			}
		}

		return mutableClaims;
	}

	private UserRecord getUserRecordByUid(String uid) {
		try {
			return firebaseAuth.getUser(uid);
		} catch (FirebaseAuthException e) {
			log.error("Error fetching user for sub '{}': {}", uid, e.getMessage(), e);
			throw ObjectNotFoundException.byUid(uid, ApplicationUser.class);
		}
	}

}
