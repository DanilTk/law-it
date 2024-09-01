package pl.lawit.kernel.model;

import lombok.NonNull;

import java.util.regex.Pattern;

import static pl.lawit.kernel.validation.ValidationUtils.require;
import static pl.lawit.kernel.validation.ValidationUtils.requireNotBlank;

public record FileName(

	@NonNull
	String value

) {

	public static final int MAX_LENGTH = 255;

	public static final Pattern ILLEGAL_CHARACTERS = Pattern.compile("[^a-zA-Z0-9\\-_.()ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+");

	public FileName {
		requireNotBlank(value);
		require(value.length() <= MAX_LENGTH, "Max length is " + MAX_LENGTH);
		require(!ILLEGAL_CHARACTERS.matcher(value).find(), "File name can only contain allowed characters");
	}

	public static FileName sanitize(@NonNull String value) {
		String sanitized = ILLEGAL_CHARACTERS.matcher(value).replaceAll("_");
		return new FileName(sanitized);
	}

	public static FileName of(String value) {
		return new FileName(value);
	}

}
