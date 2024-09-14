package pl.lawit.web.mapper;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.mime.MediaType;
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
import pl.lawit.kernel.util.FileHelper;
import pl.lawit.web.dto.MultipartFileContent;

@Component
@RequiredArgsConstructor
public class FileCommandMapper {

	private final FileHelper fileHelper;

	private final AuthenticatedUserResolver authenticatedUserResolver;

	public FileStorageCommand.UploadFileCommand mapToUploadFileCommand(MultipartFile file) {
		FileContent fileContent = MultipartFileContent.of(file);
		MediaType mediaType = fileHelper.extractMediaType(file);
		MimeType mimeType = MimeType.of(mediaType.getBaseType().toString());

		String filenameWithoutPath = FilenameUtils.getName(file.getOriginalFilename());
		FileName originalFileName = FileName.sanitize(filenameWithoutPath);

		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();
		Md5Checksum md5Checksum = fileHelper.calculateChecksum(fileContent);

		return FileStorageCommand.UploadFileCommand.builder()
			.fileContent(fileContent)
			.mimeType(mimeType)
			.fileName(originalFileName)
			.fileSize(FileSize.of(file.getSize()))
			.md5Checksum(md5Checksum)
			.authenticatedUser(authenticatedUser)
			.build();
	}

}
