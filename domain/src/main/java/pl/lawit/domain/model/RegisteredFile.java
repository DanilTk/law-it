package pl.lawit.domain.model;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.model.FileName;
import pl.lawit.kernel.model.FilePath;
import pl.lawit.kernel.model.FileSize;
import pl.lawit.kernel.model.Md5Checksum;
import pl.lawit.kernel.model.MimeType;
import pl.lawit.kernel.repository.BaseModel;

import java.net.URL;
import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record RegisteredFile(

	@NonNull
	UUID uuid,

	@NonNull
	MimeType mimeType,

	@NonNull
	FileName originalFileName,

	@NonNull
	FilePath filePath,

	@NonNull
	URL url,

	@NonNull
	FileSize fileSize,

	@NonNull
	Md5Checksum md5Checksum,

	@NonNull
	Instant createdAt,

	@NonNull
	Option<Instant> updatedAt,

	@NonNull
	UUID createdBy,

	@NonNull
	Option<UUID> updatedBy

) implements BaseModel {
}
