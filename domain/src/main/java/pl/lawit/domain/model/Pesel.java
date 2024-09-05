package pl.lawit.domain.model;

import lombok.NonNull;

import static pl.lawit.kernel.validation.ValidationUtils.require;
import static pl.lawit.kernel.validation.ValidationUtils.requireNotBlank;

public record Pesel(@NonNull String value) {

	public static final int MAX_LENGTH = 11;

	public Pesel {
		requireNotBlank(value);
		require(value.length() <= MAX_LENGTH, "max length is " + MAX_LENGTH);
	}

	public static Pesel of(@NonNull String nip) {
		return new Pesel(nip);
	}

}
