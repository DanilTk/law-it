package pl.lawit.web.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.lawit.domain.command.FileStorageCommand;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;
import pl.lawit.kernel.model.FileContent;
import pl.lawit.kernel.model.FileName;
import pl.lawit.kernel.model.FileSize;
import pl.lawit.kernel.model.Md5Checksum;
import pl.lawit.kernel.model.MimeType;
import pl.lawit.web.dto.MultipartFileContent;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class FileCommandMapper {

	private final AuthenticatedUserResolver authenticatedUserResolver;

	public FileStorageCommand.UploadFileCommand mapToUploadFileCommand(MultipartFile file) {
		FileContent fileContent = MultipartFileContent.of(file);

		MimeType mimeType = MimeType.of(file.getContentType());

		String filenameWithoutPath = FilenameUtils.getName(file.getOriginalFilename());
		FileName originalFileName = FileName.sanitize(filenameWithoutPath);

		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();
		Md5Checksum md5Checksum = calculateChecksum(fileContent);

		return FileStorageCommand.UploadFileCommand.builder()
			.fileContent(fileContent)
			.mimeType(mimeType)
			.fileName(originalFileName)
			.fileSize(FileSize.of(file.getSize()))
			.authenticatedUser(authenticatedUser)
			.md5Checksum(md5Checksum)
			.build();
	}

	@SneakyThrows
	private Md5Checksum calculateChecksum(FileContent fileContent) {
		try (InputStream inputStream = fileContent.getInputStream()) {
			String md5 = DigestUtils.md5Hex(inputStream);
			return Md5Checksum.of(md5);
		}
	}

}
