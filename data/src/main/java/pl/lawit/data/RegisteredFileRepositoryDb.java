package pl.lawit.data;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.jpa.RegisteredFileRepositoryJpa;
import pl.lawit.data.mapper.RegisteredFileMapper;
import pl.lawit.domain.command.FileStorageCommand;
import pl.lawit.domain.model.RegisteredFile;
import pl.lawit.domain.repository.RegisteredFileRepository;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@RequiredArgsConstructor
public class RegisteredFileRepositoryDb implements RegisteredFileRepository {

	private final RegisteredFileRepositoryJpa registeredFileRepositoryJpa;

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final RegisteredFileMapper registeredFileMapper;

	@Override
	@Transactional(propagation = MANDATORY)
	public RegisteredFile register(FileStorageCommand.RegisterUploadedFileCommand command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());

		RegisteredFileEntity entity = registeredFileMapper.mapToEntity(command, userEntity);

		entity = registeredFileRepositoryJpa.save(entity);

		return registeredFileMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public RegisteredFile getByUuid(UUID uuid) {
		RegisteredFileEntity entity = registeredFileRepositoryJpa.getReferenceByUuid(uuid);
		return registeredFileMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<RegisteredFile> findByUuid(UUID uuid) {
		return Option.ofOptional(registeredFileRepositoryJpa.findById(uuid)
			.map(registeredFileMapper::mapToDomain));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<RegisteredFile> findAll() {
		return List.ofAll(registeredFileRepositoryJpa.findAll())
			.map(registeredFileMapper::mapToDomain);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public void deleteByUuid(UUID uuid) {
		registeredFileRepositoryJpa.deleteById(uuid);
	}

}
