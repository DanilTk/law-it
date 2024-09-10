package pl.lawit.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

public interface LawyerDto {

	@Builder(toBuilder = true)
	record LawyerResponseDto(

		@Schema
		@NonNull
		UUID id,

		@NonNull
		String pesel,

		@NonNull
		Option<UUID> companyId,

		@NonNull
		BigDecimal hourlyRate

	) {
	}

}
