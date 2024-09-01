package pl.lawit.kernel.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class BaseException extends RuntimeException {

	protected BaseException(String message) {
		super(message);
		log.error(message);
	}

	protected BaseException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
