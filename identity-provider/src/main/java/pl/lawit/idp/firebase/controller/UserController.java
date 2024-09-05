package pl.lawit.idp.firebase.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final FirebaseAuth firebaseAuth;


    @PostMapping("/add")
    public String addUser(@RequestParam(value= "email" , required = true) String email,
                          @RequestParam (value= "password" , required = true)  String password) {
        //qwwe2131BNJNB1
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);
            UserRecord userRecord = firebaseAuth.createUser(request);
            return "User created successfully with UID: " + userRecord.getUid();
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return "Error creating user: " + e.getMessage();
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

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam (value= "uid" , required = true) String uid) {
        try {
            firebaseAuth.deleteUser(uid);
            return "User deleted successfully.";
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return "Error deleting user: " + e.getMessage();
        }
    }
//
//
//    @GetMapping("/list")
//    public List<String> listUsers(@RequestParam(value= "pageToken" , required = false) String pageToken) {
//        return firebaseUserService.listUserEmails(pageToken);
//    }


}
