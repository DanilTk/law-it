package pl.lawit.domain.model;

import lombok.NonNull;

import static pl.lawit.kernel.validation.ValidationUtils.require;
import static pl.lawit.kernel.validation.ValidationUtils.requireNotBlank;

public record CompanyNip(@NonNull String value) {

	public static final int MAX_LENGTH = 12;

	public CompanyNip {
		requireNotBlank(value);
		require(value.length() <= MAX_LENGTH, "max length is " + MAX_LENGTH);
	}

	public static CompanyNip of(@NonNull String nip) {
		return new CompanyNip(nip);
	}

}
