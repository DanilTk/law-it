package pl.lawit.idp.firebase.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

	private final FirebaseProperties firebaseProperties;

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		FileInputStream serviceAccount = new FileInputStream(firebaseProperties.getStorageAccountKey());

		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			//.setDatabaseUrl(firebaseProperties.getDbUrl())
			.build();

		return FirebaseApp.initializeApp(options);
	}

	@Bean
	public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {

		return FirebaseAuth.getInstance(firebaseApp);
	}

}
