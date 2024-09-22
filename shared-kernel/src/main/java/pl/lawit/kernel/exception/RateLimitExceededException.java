package pl.lawit.kernel.exception;

public class RateLimitExceededException extends BaseException {

	public RateLimitExceededException(String message) {
		super(message);
	}

}
