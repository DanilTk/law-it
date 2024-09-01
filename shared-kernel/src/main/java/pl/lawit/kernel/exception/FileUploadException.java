package pl.lawit.kernel.exception;

import lombok.Getter;

@Getter
public class FileUploadException extends BaseException {

	public FileUploadException() {
		super("Error while uploading file.");
	}

}
