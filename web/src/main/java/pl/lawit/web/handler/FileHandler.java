package pl.lawit.web.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.lawit.domain.model.RegisteredFile;
import pl.lawit.domain.service.FileService;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;
import pl.lawit.web.dto.FileDto.FileResponseDto;
import pl.lawit.web.mapper.FileCommandMapper;
import pl.lawit.web.mapper.FileDtoMapper;
import pl.lawit.web.validation.FileValidator;

import java.util.UUID;

import static pl.lawit.domain.command.FileStorageCommand.UploadFileCommand;

@Component
@RequiredArgsConstructor
public class FileHandler {

	private final AuthenticatedUserResolver authenticatedUserResolver;

	private final FileService fileService;

	private final FileCommandMapper commandMapper;

	private final FileValidator fileValidator;

	public FileResponseDto uploadFile(MultipartFile file) {
		fileValidator.validate(file);
		UploadFileCommand command = commandMapper.mapToUploadFileCommand(file);
		RegisteredFile registeredFile = fileService.uploadFile(command);
		return FileDtoMapper.map(registeredFile);
	}

	public FileResponseDto getFileByUuid(UUID uuid) {
		RegisteredFile registeredFile = fileService.getFile(uuid);
		return FileDtoMapper.map(registeredFile);
	}

	public void deleteFile(UUID uuid) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();
		fileService.deleteFile(uuid, authenticatedUser);
	}
}
