package pl.lawit.kernel.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

@UtilityClass
public final class ApplicationLoggerFactory {

	public static Logger rootLogger() {
		return LoggerFactory.getLogger(ROOT_LOGGER_NAME);
	}

	public static Logger emailLogger() {
		return LoggerFactory.getLogger("EMAIL_LOGGER");
	}

	public static Logger fileLogger() {
		return LoggerFactory.getLogger("FILE_LOGGER");
	}

	public static Logger userLogger() {
		return LoggerFactory.getLogger("USER_LOGGER");
	}

	public static Logger templateLogger() {
		return LoggerFactory.getLogger("TEMPLATE_LOGGER");
	}

	public static Logger caseLogger() {
		return LoggerFactory.getLogger("CASE_LOGGER");
	}

	public static Logger companyLogger() {
		return LoggerFactory.getLogger("COMPANY_LOGGER");
	}

	public static Logger paymentGatewayLogger() {
		return LoggerFactory.getLogger("PAYMENT_GATEWAY_LOGGER");
	}

	public static Logger cronLogger() {
		return LoggerFactory.getLogger("CRON_LOGGER");
	}

}
