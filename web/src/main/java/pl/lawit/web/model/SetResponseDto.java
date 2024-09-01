package pl.lawit.web.model;

import io.vavr.collection.Set;
import io.vavr.collection.SortedSet;
import lombok.NonNull;

public record SetResponseDto<T>(

	@NonNull
	Set<T> content

) {

	public static <T> SetResponseDto<T> of(Set<T> content) {
		return new SetResponseDto<>(content);
	}

	public static <T> SetResponseDto<T> ofSorted(SortedSet<T> content) {
		return new SetResponseDto<>(content);
	}

}
