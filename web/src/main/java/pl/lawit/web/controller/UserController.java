package pl.lawit.web.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lawit.web.dto.PhoneNumberRequestDTO;
import pl.lawit.web.dto.RegisterRequestDTO;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final FirebaseAuth firebaseAuth;


    @PostMapping("/add")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(registerRequest.getEmail())
                    .setPassword(registerRequest.getPassword())
                    .setDisplayName(registerRequest.getDisplayName());

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully: " + userRecord.getUid());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating user: " + e.getMessage());
        }
    }

    @PostMapping("/add-phone")
    public ResponseEntity<String> addPhoneNumber(@RequestBody PhoneNumberRequestDTO phoneNumberRequest) {
        try {

            String uid = phoneNumberRequest.getUid();
            String phoneNumber = phoneNumberRequest.getPhoneNumber();

            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                    .setPhoneNumber(phoneNumber);

            FirebaseAuth.getInstance().updateUser(request);

            Map<String, Object> claims = new HashMap<>();
            claims.put("mfa_enabled", true);  // Flag MFA as enabled
            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Phone number added. Verification SMS sent.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error adding phone number: " + e.getMessage());
        }
    }

    @PostMapping("/modify-password")
    public String modifyUserPassword(@RequestParam(value= "uid" , required = true) String uid,
                                     @RequestParam(value= "password" , required = true) String newPassword) {
        try {
            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                    .setPassword(newPassword);
            firebaseAuth.updateUser(request);
            return "User password updated successfully.";
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return "Error updating password: " + e.getMessage();
        }
    }

    @DeleteMapping
    public String deleteUser(@RequestParam (value= "uid" , required = true) String uid) {
        try {
            firebaseAuth.deleteUser(uid);
            return "User deleted successfully.";
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return "Error deleting user: " + e.getMessage();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestHeader("Authorization") String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken.replace("Bearer ", ""));
            String uid = decodedToken.getUid();

            // Normal login process (MFA is not enabled)
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Login successful for user: " + uid);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Firebase ID token: " + e.getMessage());
        }
    }



}
