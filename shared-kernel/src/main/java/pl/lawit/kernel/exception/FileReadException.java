package pl.lawit.kernel.exception;

import lombok.Getter;

@Getter
public class FileReadException extends BaseException {

	public FileReadException() {
		super("Error reading file.");
	}

}
