package pl.lawit.data.factory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.lawit.domain.command.PageCommandQuery;

@Component
public class PageableFactory {

	public Pageable create(PageCommandQuery query, Sort sort) {
		return PageRequest.of(query.pageIndex(), query.pageSize(), sort);
	}

	public Pageable create(PageCommandQuery pageQuery) {
		return PageRequest.of(pageQuery.pageIndex(), pageQuery.pageSize());
	}
}
