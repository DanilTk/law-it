package pl.lawit.web.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.web.method.HandlerTypePredicate.forBasePackage;

@Configuration
@RequiredArgsConstructor
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

	private static final String ACCOUNT_API_WEB_BASE_PACKAGE = "pl.lawit.web";

	private static final String ACCOUNT_API_BASE_PACKAGE = "pl.lawit.api";

	private final BackendWebMvcProperties backendWebMvcProperties;

	@Override
	public void configurePathMatch(PathMatchConfigurer conf) {
		conf.addPathPrefix(backendWebMvcProperties.getApiWebPrefix(), forBasePackage(ACCOUNT_API_WEB_BASE_PACKAGE))
			.addPathPrefix(backendWebMvcProperties.getApiPrefix(), forBasePackage(ACCOUNT_API_BASE_PACKAGE));
	}

}
