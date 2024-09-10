package pl.lawit.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

import java.net.URL;
import java.time.Instant;
import java.util.UUID;

public interface FileDto {

	@Builder(toBuilder = true)
	record FileResponseDto(

		@Schema
		@NonNull
		UUID id,

		@Schema
		@NonNull
		String fileName,

		@Schema(description = "MIME type of the file (e.g., image/jpeg, application/pdf)")
		@NonNull
		String mimeType,

		@Schema(description = "URL to download the file")
		@NonNull
		URL fileUrl,

		@Schema(description = "Size of the uploaded file in bytes")
		@NonNull
		Long fileSize,

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
		@Schema
		@NonNull
		Instant createdAt

	) {
	}

}
