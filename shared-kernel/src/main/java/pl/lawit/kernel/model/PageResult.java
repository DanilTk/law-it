package pl.lawit.kernel.model;

import io.vavr.collection.List;
import lombok.Builder;
import lombok.NonNull;

import java.util.function.Function;

@Builder(toBuilder = true)
public record PageResult<T>(

	@NonNull
	List<T> content,

	@NonNull
	Integer pageSize,

	@NonNull
	Integer pageIndex,

	@NonNull
	Integer totalPages,

	@NonNull
	Long totalElements

) {

	public <R> PageResult<R> map(Function<? super T, ? extends R> mapper) {
		return PageResult.<R>builder()
			.content(content().map(mapper))
			.pageSize(pageSize)
			.pageIndex(pageIndex)
			.totalPages(totalPages)
			.totalElements(totalElements)
			.build();
	}

}
