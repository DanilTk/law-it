package pl.lawit.domain.repository;

import pl.lawit.domain.model.RegisteredFile;
import pl.lawit.kernel.repository.BaseRepository;

import java.util.UUID;

import static pl.lawit.domain.command.FileStorageCommand.RegisterUploadedFileCommand;

public interface RegisteredFileRepository extends BaseRepository<RegisteredFile> {

	RegisteredFile register(RegisterUploadedFileCommand command);

	void deleteByUuid(UUID uuid);

}
