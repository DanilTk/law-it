package pl.lawit.kernel.model;

import lombok.NonNull;

import static pl.lawit.kernel.validation.ValidationUtils.require;
import static pl.lawit.kernel.validation.ValidationUtils.requireNotBlank;

public record FilePath(

	@NonNull
	String value

) {

	public static final int MAX_LENGTH = 255;

	public static FilePath of(String value) {
		return new FilePath(value);
	}

	public FilePath {
		requireNotBlank(value);
		require(value.length() <= MAX_LENGTH, "Max length is " + MAX_LENGTH);
	}

}
