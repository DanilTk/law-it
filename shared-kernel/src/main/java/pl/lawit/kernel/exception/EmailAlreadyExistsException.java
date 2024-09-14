package pl.lawit.kernel.exception;

import lombok.Getter;

@Getter
public class EmailAlreadyExistsException extends BaseException {

	public EmailAlreadyExistsException() {
		super("Email is already taken.");
	}

}
