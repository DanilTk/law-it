package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.RegisteredFileEntity;
import pl.lawit.domain.model.RegisteredFile;

import static pl.lawit.domain.command.FileStorageCommand.RegisterUploadedFileCommand;

@Mapper(componentModel = "spring")
public abstract class RegisteredFileMapper extends BaseMapper {

	@Mapping(target = "uuid", source = "uuid")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", expression = "java(ofOption(entity.getUpdatedAt()))")
	@Mapping(target = "createdBy", expression = "java(extractUserUuid(entity.getCreatedBy()))")
	@Mapping(target = "updatedBy", expression = "java(ofOption(entity.getUpdatedBy()).map($->$.getUuid()))")
	@Mapping(target = "mimeType", expression = "java(MimeType.of(entity.getMimeType()))")
	@Mapping(target = "originalFileName", expression = "java(FileName.of(entity.getOriginalFileName()))")
	@Mapping(target = "filePath", expression = "java(FilePath.of(entity.getFilePath()))")
	@Mapping(target = "url", expression = "java(ofUrl(entity.getFileUrl()))")
	@Mapping(target = "fileSize", expression = "java(FileSize.of(entity.getFileSize()))")
	@Mapping(target = "md5Checksum", expression = "java(Md5Checksum.of(entity.getMd5Checksum()))")
	public abstract RegisteredFile mapToDomain(RegisteredFileEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", source = "userEntity")
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "mimeType", source = "command.mimeType.value")
	@Mapping(target = "originalFileName", source = "command.fileName.value")
	@Mapping(target = "filePath", source = "command.filePath.value")
	@Mapping(target = "fileUrl", expression = "java(command.url().toString())")
	@Mapping(target = "fileSize", source = "command.fileSize.value")
	@Mapping(target = "md5Checksum", source = "command.md5Checksum.value")
	public abstract RegisteredFileEntity mapToEntity(RegisterUploadedFileCommand command,
													 ApplicationUserEntity userEntity);

}
