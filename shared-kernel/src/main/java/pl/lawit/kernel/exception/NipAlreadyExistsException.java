package pl.lawit.kernel.exception;

import lombok.Getter;

@Getter
public class NipAlreadyExistsException extends BaseException {

	public NipAlreadyExistsException() {
		super("NIP is already taken.");
	}

}
