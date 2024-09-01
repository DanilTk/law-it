package pl.lawit.web.model;

import io.vavr.collection.List;
import lombok.NonNull;

public record ListResponseDto<T>(

	@NonNull
	List<T> content

) {

	public static <T> ListResponseDto<T> of(@NonNull List<T> content) {
		return new ListResponseDto<>(content);
	}

	public static <T> ListResponseDto<T> of(@NonNull T[] array) {
		return new ListResponseDto<>(List.of(array));
	}

}
