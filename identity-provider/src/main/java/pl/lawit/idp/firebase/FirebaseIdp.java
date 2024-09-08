package pl.lawit.idp.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.PageResultFireBase;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.domain.provider.IdpProvider;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.kernel.model.EmailAddress;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FirebaseIdp implements IdpProvider {

    private static String ROLES = "roles";

    private final FirebaseAuth firebaseAuth;
    private final UserRoleResolver userRoleResolver;


    @Override
    public ApplicationUser getBySub(String sub) throws FirebaseAuthException {

        UserRecord userRecord = firebaseAuth.getUser(sub);

        return convertToApplicationUser(userRecord);
    }

    @Override
    public ApplicationUser getByEmail(EmailAddress email) throws FirebaseAuthException {

        UserRecord userRecord = firebaseAuth.getUserByEmail(email.value());

        return ApplicationUser.builder()
                .idpSub(userRecord.getUid())
                .email(new EmailAddress(userRecord.getEmail()))
                .isIdpUser(true)
                .build();

    }

    @Override
    public Set<ApplicationUserRole> getUserRoles(String sub) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUser(sub);
        Map<String, Object> claims = userRecord.getCustomClaims();
        java.util.List<String> existingRoles = (java.util.List<String>) claims.get(ROLES);

        List<String> roles = List.ofAll(existingRoles);

        return userRoleResolver.resolveUserRoles(roles);
    }


    @Override
    public ApplicationUser updateUserAccess(String sub, ApplicationUserRole role) throws FirebaseAuthException {

        UserRecord userRecord = FirebaseAuth.getInstance().getUser(sub);
        Map<String, Object> claims = userRecord.getCustomClaims();

        Map<String, Object> updatedClaims = setUserClaim(claims, role.name());

        FirebaseAuth.getInstance().setCustomUserClaims(sub, updatedClaims);

        return convertToApplicationUser(userRecord);
    }

    @Override
    public PageResultFireBase<ApplicationUser> findPage(String pageToken, int pageSize) throws FirebaseAuthException {

        ListUsersPage page = firebaseAuth.listUsers(pageToken, pageSize);
        List<UserRecord> userRecordList = List.ofAll(page.getValues());


        List<ApplicationUser> applicationUserList = userRecordList.map(userRecord -> {
            return ApplicationUser.builder()
                    .idpSub(userRecord.getUid())
                    .email(new EmailAddress(userRecord.getEmail()))
                    .isIdpUser(userRecord.isEmailVerified())
                    .idpSub(userRecord.getUid())
                    .createdAt(Instant.ofEpochSecond(userRecord.getUserMetadata().getCreationTimestamp() / 1000))
                    .build();
        });


        return new PageResultFireBase<>(
                applicationUserList,
                page.getNextPageToken()
        );

    }

    @Override
    public void revokeUserAccess(String sub) throws FirebaseAuthException {

        UserRecord userRecord = firebaseAuth.getUser(sub);
        Map<String, Object> claims = userRecord.getCustomClaims();

        Map<String, Object> mutableClaims = new HashMap<>();

        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            if (!entry.getKey().equals("roles")) {
                mutableClaims.put(entry.getKey(), entry.getValue());
            }
        }

        firebaseAuth.setCustomUserClaims(sub, mutableClaims);

    }

    private Map<String, Object> setUserClaim(Map<String, Object> claims, String value) throws FirebaseAuthException {
        Map<String, Object> mutableClaims = new HashMap<>();

        List<String> specificClaimList = List.of(value);

        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            if (entry.getKey().equals("roles")) {
                mutableClaims.put(entry.getKey(), specificClaimList);
            } else {
                mutableClaims.put(entry.getKey(), entry.getValue());
            }
        }

        return mutableClaims;
    }


    private ApplicationUser convertToApplicationUser(UserRecord userRecord) {
        return ApplicationUser.builder()
                .idpSub(userRecord.getUid())
                .email(new EmailAddress(userRecord.getEmail()))
                .isIdpUser(userRecord.isEmailVerified())
                .idpSub(userRecord.getUid())
                .createdAt(Instant.ofEpochSecond(userRecord.getUserMetadata().getCreationTimestamp() / 1000))
                .build();
    }


}
