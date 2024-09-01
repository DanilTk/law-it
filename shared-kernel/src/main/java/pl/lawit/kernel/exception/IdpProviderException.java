package pl.lawit.kernel.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class IdpProviderException extends BaseException {

	@NonNull
	private final String friendlyMessage;

	public IdpProviderException(@NonNull String friendlyMessage) {
		super(friendlyMessage);
		this.friendlyMessage = friendlyMessage;
	}

}
