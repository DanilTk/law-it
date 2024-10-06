package pl.lawit.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@EnableConfigurationProperties
@EntityScan(basePackages = "pl.lawit.data")
@EnableMongoRepositories(basePackages = "pl.lawit.data")
@EnableJpaRepositories(basePackages = "pl.lawit.data")
@SpringBootApplication(scanBasePackages = "pl.lawit")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
