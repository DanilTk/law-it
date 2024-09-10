package pl.lawit.kernel.model;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;

import java.util.function.Function;

@Builder(toBuilder = true)
public record TokenizedPageResult<T>(

	@NonNull
	List<T> content,

	@NonNull
	Option<String> nextPageToken

) {
	public <R> TokenizedPageResult<R> map(Function<? super T, ? extends R> mapper) {
		return TokenizedPageResult.<R>builder()
			.content(content.map(mapper))
			.nextPageToken(nextPageToken)
			.build();
	}

}
