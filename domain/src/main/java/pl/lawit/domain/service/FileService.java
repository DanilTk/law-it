package pl.lawit.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.command.FileStorageCommand.UploadFileCommand;
import pl.lawit.domain.model.RegisteredFile;
import pl.lawit.domain.repository.RegisteredFileRepository;
import pl.lawit.domain.storage.FileStorage;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.StoredFile;

import java.net.URL;
import java.util.UUID;

import static pl.lawit.domain.command.FileStorageCommand.RegisterUploadedFileCommand;
import static pl.lawit.domain.event.TransactionalFileEvent.DeleteFileEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

	private final FileStorage fileStorage;

	private final RegisteredFileRepository registeredFileRepository;

	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public RegisteredFile uploadFile(UploadFileCommand command) {
		StoredFile storedFile = fileStorage.uploadFile(command);

		RegisterUploadedFileCommand repositoryCommand = RegisterUploadedFileCommand.builder()
			.mimeType(command.mimeType())
			.fileName(command.fileName())
			.fileSize(command.fileSize())
			.authenticatedUser(command.authenticatedUser())
			.md5Checksum(command.md5Checksum())
			.filePath(storedFile.filePath())
			.url(storedFile.url())
			.build();

		RegisteredFile registeredFile = registeredFileRepository.register(repositoryCommand);
		URL presignedUrl = fileStorage.getPresignedUrl(registeredFile.filePath());

		log.info("Registered uploaded file with uuid: {}", registeredFile.uuid());

		return registeredFile.toBuilder()
			.url(presignedUrl)
			.build();
	}

	@Transactional
	public RegisteredFile getFile(UUID uuid) {
		RegisteredFile registeredFile = registeredFileRepository.getByUuid(uuid);
		URL presignedUrl = fileStorage.getPresignedUrl(registeredFile.filePath());

		return registeredFile.toBuilder()
			.url(presignedUrl)
			.build();
	}

	@Transactional
	public void deleteFile(UUID uuid, AuthenticatedUser authenticatedUser) {
		RegisteredFile registeredFile = registeredFileRepository.getByUuid(uuid);
		deleteFile(registeredFile);
		log.info("Deleted file with companyUuid: {} by user: {}", uuid, authenticatedUser.userUuid());
	}

	private void deleteFile(RegisteredFile registeredFile) {
		registeredFileRepository.deleteByUuid(registeredFile.uuid());
		DeleteFileEvent event = DeleteFileEvent.of(registeredFile.filePath());
		eventPublisher.publishEvent(event);
	}

}
