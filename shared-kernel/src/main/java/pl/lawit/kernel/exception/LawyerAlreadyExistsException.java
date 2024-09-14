package pl.lawit.kernel.exception;

import lombok.Getter;

@Getter
public class LawyerAlreadyExistsException extends BaseException {

	public LawyerAlreadyExistsException() {
		super("Lawyer already exists.");
	}

}
