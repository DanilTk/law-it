package pl.lawit.kernel.exception;

import lombok.Getter;

import static pl.lawit.kernel.logger.ApplicationLoggerFactory.rootLogger;

@Getter
public abstract class BaseException extends RuntimeException {

	protected BaseException(String message) {
		super(message);
		rootLogger().error(message);
	}

	protected BaseException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
