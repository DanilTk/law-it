package pl.lawit.gateway.configuration.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CaffeineConfiguration {

	@Bean
	public Caffeine<Object, Object> caffeineConfig() {
		return Caffeine.newBuilder()
			.maximumSize(1000);
	}

	@Bean
	public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager("payment-gateway-token");
		cacheManager.setCaffeine(caffeine);
		return cacheManager;
	}

}
