package pl.lawit.data.factory;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.lawit.domain.command.PageCommandQuery;

@UtilityClass
public final class PageableFactory {

	public static Pageable create(PageCommandQuery commandQuery, Sort sort) {
		return PageRequest.of(commandQuery.pageIndex(), commandQuery.pageSize(), sort);
	}

	public static Pageable create(PageCommandQuery commandQuery) {
		return PageRequest.of(commandQuery.pageIndex(), commandQuery.pageSize());
	}
}
