package pl.lawit.web.mapper;

import pl.lawit.domain.model.RegisteredFile;

import static pl.lawit.web.dto.FileDto.FileResponseDto;

public class FileDtoMapper {

	private FileDtoMapper() {
	}

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
