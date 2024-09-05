package pl.lawit.idp.firebase.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FirebaseUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
		try {
			FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
			String uid = decodedToken.getUid();
			// Here you might fetch user details from a database using UID
			return org.springframework.security.core.userdetails.User
				.withUsername(uid)
				.password("")  // Password is not used in this case
				.roles("USER")
				.build();
		} catch (FirebaseAuthException e) {
			throw new UsernameNotFoundException("Invalid token.", e);
		}
	}
}