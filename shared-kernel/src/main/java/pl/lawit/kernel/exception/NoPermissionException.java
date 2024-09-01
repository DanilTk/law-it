package pl.lawit.kernel.exception;

public class NoPermissionException extends BaseException {

	public NoPermissionException() {
		super("No permission");
	}

	public NoPermissionException(String message) {
		super(message);
	}

	public NoPermissionException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
