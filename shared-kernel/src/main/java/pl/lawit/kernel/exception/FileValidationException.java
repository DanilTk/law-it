package pl.lawit.kernel.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class FileValidationException extends BaseException {

	@NonNull
	private final String friendlyMessage;

	public FileValidationException(@NonNull String friendlyMessage) {
		super(friendlyMessage);
		this.friendlyMessage = friendlyMessage;
	}

}
