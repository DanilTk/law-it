package pl.lawit.kernel.exception;

import lombok.Getter;

@Getter
public class PeselAlreadyExistsException extends BaseException {

	public PeselAlreadyExistsException() {
		super("PESEL is already taken.");
	}

}
