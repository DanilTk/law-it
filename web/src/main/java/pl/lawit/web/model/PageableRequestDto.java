package pl.lawit.web.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageableRequestDto {

	public static final int DEFAULT_PAGE_INDEX = 0;

	public static final int DEFAULT_PAGE_SIZE = 10;

	@Parameter(description = "Page index. Default: " + DEFAULT_PAGE_INDEX)
	@Min(0)
	private Integer pageIndex;

	@Parameter(description = "Page size. Default: " + DEFAULT_PAGE_SIZE)
	@Min(1)
	@Max(50)
	private Integer pageSize;

}
