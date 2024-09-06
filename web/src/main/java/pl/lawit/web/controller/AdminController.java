package pl.lawit.web.controller;

import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.lawit.idp.firebase.services.UserManagementService;
import pl.lawit.web.dto.ClaimDto;

import java.security.Permission;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserManagementService userManagementService;

    @Secured("ROLE_ANONYMOUS")
    @PostMapping(path = "/user-role/{uid}")
    public void setRole(
            @PathVariable String uid,
            @RequestBody ClaimDto claim
    ) throws FirebaseAuthException {
        userManagementService.setUserClaims(uid, claim.getClaimName(), claim.getApplicationUserRole().name());
    }

    @Secured("ADMIN_USER")
    @GetMapping(path = "/user-claims/{uid}")
    public ResponseEntity<?> getUserClaims(
            @PathVariable String uid) throws FirebaseAuthException {
        return ResponseEntity.ok(userManagementService.getUserClaims(uid));
    }

    @Secured("ROLE_ANONYMOUS")
    @DeleteMapping(path = "/user-claim/{uid}")
    public ResponseEntity<?> deleteClaims(
            @PathVariable String uid
    ) throws FirebaseAuthException {
        return ResponseEntity.ok(userManagementService.deleteUserClaims(uid));
    }


}