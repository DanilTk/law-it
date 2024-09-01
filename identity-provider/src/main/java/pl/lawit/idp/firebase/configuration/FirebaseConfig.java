package pl.lawit.idp.firebase.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

	@Value("${firebase.serviceAccountKey}")
	private String serviceAccountKey;

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		FileInputStream serviceAccount = new FileInputStream(serviceAccountKey);

		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.build();

		return FirebaseApp.initializeApp(options);
	}

	@Bean
	public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
		return FirebaseAuth.getInstance(firebaseApp);
	}

}
