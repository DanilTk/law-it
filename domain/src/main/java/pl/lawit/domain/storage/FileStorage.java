package pl.lawit.domain.storage;

import pl.lawit.kernel.model.FilePath;
import pl.lawit.kernel.model.StoredFile;

import java.io.InputStream;
import java.net.URL;

import static pl.lawit.domain.command.FileStorageCommand.UploadFileCommand;

public interface FileStorage {

	StoredFile uploadFile(UploadFileCommand command);

	InputStream downloadFile(FilePath filePath);

	URL getPresignedUrl(FilePath filePath);

}
