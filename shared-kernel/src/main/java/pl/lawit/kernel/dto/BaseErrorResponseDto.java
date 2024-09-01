package pl.lawit.kernel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
public class BaseErrorResponseDto {

	@Schema
	private int statusCode;

	@Schema
	@NonNull
	private String message;

	@Schema
	@Builder.Default
	@NonNull
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Instant timestamp = Instant.now();

	@Schema
	@NonNull
	private UUID errorId;

}
