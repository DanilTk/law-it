package pl.lawit.kernel.model;

import lombok.NonNull;

import static pl.lawit.kernel.validation.ValidationUtils.require;
import static pl.lawit.kernel.validation.ValidationUtils.requireNotBlank;

public record MimeType(

	@NonNull
	String value

) {

	public static final int MAX_LENGTH = 255;

	public MimeType {
		requireNotBlank(value);
		require(value.length() <= MAX_LENGTH, "Max length is " + MAX_LENGTH);
	}

	public static MimeType of(String value) {
		return new MimeType(value);
	}

}
