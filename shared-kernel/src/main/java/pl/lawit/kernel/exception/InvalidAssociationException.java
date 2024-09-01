package pl.lawit.kernel.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class InvalidAssociationException extends BaseException {

	@NonNull
	private final String friendlyMessage;

	public InvalidAssociationException(@NonNull String friendlyMessage) {
		super(friendlyMessage);
		this.friendlyMessage = friendlyMessage;
	}

}
