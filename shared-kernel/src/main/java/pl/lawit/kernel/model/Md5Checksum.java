package pl.lawit.kernel.model;

import lombok.NonNull;

import java.util.regex.Pattern;

import static pl.lawit.kernel.validation.ValidationUtils.require;
import static pl.lawit.kernel.validation.ValidationUtils.requireNotBlank;

public record Md5Checksum(

	@NonNull
	String value

) {

	public static final int LENGTH = 32;

	public static final Pattern PATTERN = Pattern.compile("[0-9a-f]{32}");

	public static Md5Checksum of(String value) {
		return new Md5Checksum(value);
	}

	public Md5Checksum {
		requireNotBlank(value);
		require(value.length() == LENGTH, "Length must be: " + LENGTH);
		require(PATTERN.matcher(value).matches(), "MD5 checksum can only contain digits and letters a-f");
	}

}
