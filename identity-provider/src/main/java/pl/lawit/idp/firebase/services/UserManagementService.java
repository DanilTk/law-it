package pl.lawit.idp.firebase.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final FirebaseAuth firebaseAuth;

    public void setUserClaims(String uid, String claim, String value) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUser(uid);
        Map<String, Object> claims = userRecord.getCustomClaims();

        Map<String, Object> mutableClaims =  new HashMap<>();
        mutableClaims.put(claim, value);

        if (claims != null) {
            mutableClaims.putAll(claims);
        }


        firebaseAuth.setCustomUserClaims(uid, mutableClaims);
    }


    public Map<String, Object> getUserClaims(String uid) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUser(uid);

        return userRecord.getCustomClaims();

    }


    public Map<String, Object> deleteUserClaims(String uid) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUser(uid);
        Map<String, Object> claims = userRecord.getCustomClaims();

        Map<String, Object> mutableClaims = new HashMap<>();

        firebaseAuth.setCustomUserClaims(uid, mutableClaims);
        return mutableClaims;

    }

}
