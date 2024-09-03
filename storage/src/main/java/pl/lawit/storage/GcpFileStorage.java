package pl.lawit.storage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.lawit.domain.event.TransactionalFileEvent.DeleteFileEvent;
import pl.lawit.domain.storage.EventDrivenFileStorage;
import pl.lawit.kernel.exception.FileReadException;
import pl.lawit.kernel.exception.FileUploadException;
import pl.lawit.kernel.model.FilePath;
import pl.lawit.kernel.model.StoredFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import static com.google.cloud.storage.HttpMethod.GET;
import static com.google.cloud.storage.Storage.SignUrlOption.httpMethod;
import static com.google.cloud.storage.Storage.SignUrlOption.withV4Signature;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMPLETION;
import static pl.lawit.domain.command.FileStorageCommand.UploadFileCommand;

@Slf4j
@Component
@RequiredArgsConstructor
public class GcpFileStorage implements EventDrivenFileStorage {

	private final Storage storage;

	private final FilePathProvider filePathProvider;

	@Value("${spring.cloud.gcp.storage.bucket-name}")
	private String bucketName;

	@Value("${spring.cloud.gcp.storage.domain}")
	private String storageDomain;

	@Override
	public StoredFile uploadFile(UploadFileCommand command) {
		FilePath filePath = filePathProvider.createBlobFilePath(command.fileName());
		BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, filePath.value())
			.setContentType(command.mimeType().value())
			.setMd5(command.md5Checksum().value())
			.build();

		try (InputStream inputStream = command.fileContent().getInputStream()) {
			byte[] content = readBytes(inputStream);
			storage.create(blobInfo, content);
			URL fileUrl = getFileUrl(filePath);
			log.info("Uploaded blob: {}", filePath.value());
			return new StoredFile(filePath, fileUrl);
		} catch (IOException e) {
			throw new FileUploadException();
		}

	}

	@Override
	public InputStream downloadFile(FilePath filePath) {
		BlobId blobId = BlobId.of(bucketName, filePath.value());
		Blob blob = storage.get(blobId);

		if (blob == null || !blob.exists()) {
			throw new FileReadException();
		}

		return new ByteArrayInputStream(blob.getContent());
	}

	@SneakyThrows
	private URL getFileUrl(FilePath filePath) {
		return new URI(storageDomain + bucketName + '/' + filePath.value()).toURL();
	}

	@Override
	public URL getPresignedUrl(FilePath filePath) {
		BlobId blobId = BlobId.of(bucketName, filePath.value());
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		return storage.signUrl(blobInfo, 5, MINUTES, withV4Signature(), httpMethod(GET));
	}

	@Override
	@TransactionalEventListener(phase = AFTER_COMPLETION)
	public void deleteFile(DeleteFileEvent event) {
		boolean isDeleted = storage.delete(bucketName, event.filePath().value());
		if (isDeleted) {
			log.info("Deleted blob: {}", event.filePath().value());
		}
	}

	private byte[] readBytes(InputStream inputStream) throws IOException {
		return IOUtils.toByteArray(inputStream);
	}

}