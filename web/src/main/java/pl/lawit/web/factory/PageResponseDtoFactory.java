package pl.lawit.web.factory;

import io.vavr.collection.List;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.web.dto.PageResponseDto;

import java.util.function.Function;

@Component
public class PageResponseDtoFactory {

	public <T> PageResponseDto<T> create(PageResult<T> page) {
		return PageResponseDto.<T>builder()
			.content(page.content())
			.pageSize(page.pageSize())
			.pageIndex(page.pageIndex())
			.totalPages(page.totalPages())
			.totalElements(page.totalElements())
			.build();
	}

	public <T, C> PageResponseDto<C> create(PageResult<T> page, List<C> content) {
		return PageResponseDto.<C>builder()
			.content(content)
			.pageSize(page.pageSize())
			.pageIndex(page.pageIndex())
			.totalPages(page.totalPages())
			.totalElements(page.totalElements())
			.build();
	}

	public <T, R> PageResponseDto<R> create(PageResult<T> page, Function<? super T, ? extends R> contentMapper) {
		List<R> content = page.content()
			.map(contentMapper);

		return PageResponseDto.<R>builder()
			.content(content)
			.pageSize(page.pageSize())
			.pageIndex(page.pageIndex())
			.totalPages(page.totalPages())
			.totalElements(page.totalElements())
			.build();
	}

}
