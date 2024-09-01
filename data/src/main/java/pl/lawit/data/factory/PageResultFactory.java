package pl.lawit.data.factory;

import io.vavr.collection.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.model.PageResult;

@Component
public class PageResultFactory {

	public <T> PageResult<T> create(Page<T> page) {
		return PageResult.<T>builder()
			.content(List.ofAll(page.getContent()))
			.pageSize(page.getSize())
			.pageIndex(page.getNumber())
			.totalPages(page.getTotalPages())
			.totalElements(page.getTotalElements())
			.build();
	}

}
