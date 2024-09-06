package pl.lawit.web.controller;

import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN_USER')")
    @PostMapping(path = "/user-role/{uid}")
    public void setRole(
            @PathVariable String uid,
            @RequestBody ClaimDto claim
    ) throws FirebaseAuthException {
        userManagementService.setUserClaims(uid, claim.getClaimName(), claim.getApplicationUserRole().name());
    }

    @PreAuthorize("hasRole('ADMIN_USER')")
    @GetMapping(path = "/user-claims/{uid}")
    public ResponseEntity<?> getUserClaims(
            @PathVariable String uid) throws FirebaseAuthException {
        return ResponseEntity.ok(userManagementService.getUserClaims(uid));
    }

    @PreAuthorize("hasRole('ADMIN_USER')")
    @DeleteMapping(path = "/user-claim/{uid}")
    public ResponseEntity<?> deleteClaims(
            @PathVariable String uid
    ) throws FirebaseAuthException {
        return ResponseEntity.ok(userManagementService.deleteUserClaims(uid));
    }


}