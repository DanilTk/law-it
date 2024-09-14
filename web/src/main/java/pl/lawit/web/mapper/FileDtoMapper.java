package pl.lawit.web.mapper;

import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.RegisteredFile;

import static pl.lawit.web.dto.FileDto.FileResponseDto;

@UtilityClass
public final class FileDtoMapper {

	public static FileResponseDto map(RegisteredFile registeredFile) {
		return FileResponseDto.builder()
			.id(registeredFile.uuid())
			.mimeType(registeredFile.mimeType().value())
			.fileName(registeredFile.originalFileName().value())
			.fileUrl(registeredFile.url())
			.fileSize(registeredFile.fileSize().value())
			.createdAt(registeredFile.createdAt())
			.build();
	}

}
