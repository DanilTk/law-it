package pl.lawit.kernel.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class PaymentProviderException extends BaseException {

	@NonNull
	private final String friendlyMessage;

	public PaymentProviderException(@NonNull String friendlyMessage) {
		super(friendlyMessage);
		this.friendlyMessage = friendlyMessage;
	}

}
