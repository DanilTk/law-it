package pl.lawit.domain.command;

import lombok.Builder;
import lombok.NonNull;

import static pl.lawit.kernel.validation.ValidationUtils.require;

@Builder(toBuilder = true)
public record PageCommandQuery(

	@NonNull
	Integer pageIndex,

	@NonNull
	Integer pageSize

) {

	public PageCommandQuery {
		require(pageIndex >= 0, "pageIndex cannot be negative");
		require(pageSize > 0, "pageSize must be greater than zero");
	}

}
