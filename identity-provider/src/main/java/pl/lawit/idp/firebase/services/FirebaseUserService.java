package pl.lawit.idp.firebase.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebaseUserService {

    private final FirebaseAuth firebaseAuth;

    public List<String> listUserEmails(String pageToken) {
        List<String> emails = new ArrayList<>();
        try {
            ListUsersPage listUsersPage = firebaseAuth.listUsers(pageToken);
            for (UserRecord userRecord : listUsersPage.getValues()) {
                emails.add(userRecord.getEmail());
            }
            // Return the next page token if there are more pages
            return emails;
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return List.of("Error fetching users: " + e.getMessage());
        }
    }
}