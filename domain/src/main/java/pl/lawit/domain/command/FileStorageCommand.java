package pl.lawit.domain.command;

import ch.qos.logback.core.util.FileSize;
import lombok.Builder;
import lombok.NonNull;
import org.apache.pdfbox.util.filetypedetector.FileType;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.FileContent;
import pl.lawit.kernel.model.FileName;
import pl.lawit.kernel.model.Md5Checksum;
import pl.lawit.kernel.model.MimeType;

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
		FileType fileType,

		@NonNull
		Md5Checksum md5Checksum,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {

	}

}
