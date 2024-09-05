package pl.lawit.kernel.model;

import lombok.NonNull;

import static pl.lawit.kernel.validation.ValidationUtils.require;

public record FileSize(

	@NonNull
	Long value

) {

	public static FileSize of(Long value) {
		return new FileSize(value);
	}

	public FileSize {
		require(value >= 0, "File size cannot be negative");
	}

}
