package pl.lawit.web.mapper;

import io.vavr.control.Option;
import lombok.experimental.UtilityClass;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.web.dto.PageableRequestDto;

@UtilityClass
public final class PageCommandMapper {

	public static PageCommandQuery map(PageableRequestDto dto) {
		Integer pageIndex = Option.of(dto.getPageIndex())
			.getOrElse(PageableRequestDto.DEFAULT_PAGE_INDEX);

		Integer pageSize = Option.of(dto.getPageSize())
			.getOrElse(PageableRequestDto.DEFAULT_PAGE_SIZE);

		return new PageCommandQuery(pageIndex, pageSize);
	}

}
