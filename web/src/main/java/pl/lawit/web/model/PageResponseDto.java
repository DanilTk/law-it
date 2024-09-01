package pl.lawit.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.vavr.collection.List;
import lombok.Builder;
import lombok.NonNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder(toBuilder = true)
public record PageResponseDto<T>(

	@Schema(requiredMode = REQUIRED)
	@NonNull
	List<T> content,

	@Schema(requiredMode = REQUIRED)
	@NonNull
	Integer pageSize,

	@Schema(requiredMode = REQUIRED)
	@NonNull
	Integer pageIndex,

	@Schema(requiredMode = REQUIRED)
	@NonNull
	Integer totalPages,

	@Schema(requiredMode = REQUIRED, description = "Estimated total number of elements")
	@NonNull
	Long totalElements

) {

}
