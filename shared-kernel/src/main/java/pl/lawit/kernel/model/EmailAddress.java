package pl.lawit.kernel.model;

import lombok.NonNull;

import java.util.regex.Pattern;

import static pl.lawit.kernel.validation.ValidationUtils.require;
import static pl.lawit.kernel.validation.ValidationUtils.requireNotBlank;

public record EmailAddress(

	@NonNull
	String value

) {

	public static final int MAX_LENGTH = 255;

	//Liberal regex checking if the format is: user@host.domain.
	public static final String REGEX = "[^@\\s]+@[^@\\s.]+\\.[^@\\s]+";

	//Liberal regex checking if the format is: user@host.domain or USER@HOST.DOMAIN.
	//public static final String REGEX = "/^[^\\s@]+@[^\\s@.]+\\.[^\\s@]+$/i";

	public static final Pattern PATTERN = Pattern.compile(REGEX);

	public EmailAddress {
		requireNotBlank(value);
		require(value.length() <= MAX_LENGTH, "max length is " + MAX_LENGTH);
		require(PATTERN.matcher(value).matches(), "e-mail must be in correct format");
	}

	public static EmailAddress of(@NonNull String value) {
		return new EmailAddress(value);
	}

}
