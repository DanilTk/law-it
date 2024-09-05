package pl.lawit.domain.command;

import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.FileContent;
import pl.lawit.kernel.model.FileName;
import pl.lawit.kernel.model.FilePath;
import pl.lawit.kernel.model.FileSize;
import pl.lawit.kernel.model.Md5Checksum;
import pl.lawit.kernel.model.MimeType;

import java.net.URL;

public interface FileStorageCommand {

	@Builder(toBuilder = true)
	record UploadFileCommand(

		@NonNull
		FileContent fileContent,

		@NonNull
		MimeType mimeType,

		@NonNull
		FileName fileName,

		@NonNull
		FileSize fileSize,

		@NonNull
		Md5Checksum md5Checksum,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {

	}

	@Builder(toBuilder = true)
	record RegisterUploadedFileCommand(

		@NonNull
		MimeType mimeType,

		@NonNull
		FileName fileName,

		@NonNull
		FileSize fileSize,

		@NonNull
		Md5Checksum md5Checksum,

		@NonNull
		AuthenticatedUser authenticatedUser,

		@NonNull
		FilePath filePath,

		@NonNull
		URL url

	) {

	}

}
