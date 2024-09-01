package pl.lawit.kernel.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class BusinessException extends BaseException {

	@NonNull
	private final String friendlyMessage;

	public BusinessException(@NonNull String friendlyMessage) {
		super(friendlyMessage);
		this.friendlyMessage = friendlyMessage;
	}

}
