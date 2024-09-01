package pl.lawit.web.configuration;

import lombok.SneakyThrows;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TikaConfiguration {

	@Bean
	@SneakyThrows
	public Detector contentTypeDetector() {
		return new TikaConfig().getDetector();
	}
}
