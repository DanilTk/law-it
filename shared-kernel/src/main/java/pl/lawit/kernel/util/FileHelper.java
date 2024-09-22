package pl.lawit.kernel.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.lawit.kernel.exception.FileValidationException;
import pl.lawit.kernel.model.FileContent;
import pl.lawit.kernel.model.FileDetail;
import pl.lawit.kernel.model.Md5Checksum;
import pl.lawit.kernel.model.MimeType;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.tika.metadata.TikaCoreProperties.RESOURCE_NAME_KEY;
import static pl.lawit.kernel.logger.ApplicationLoggerFactory.fileLogger;

@Component
@RequiredArgsConstructor
public class FileHelper {

	private final Detector contentTypeDetector;

	@SneakyThrows
	public Md5Checksum calculateChecksum(FileContent fileContent) {
		try (InputStream inputStream = fileContent.getInputStream()) {
			String md5 = DigestUtils.md5Hex(inputStream);
			return Md5Checksum.of(md5);
		}
	}

	@SneakyThrows
	public MediaType extractMediaType(MultipartFile file) {
		Metadata metadata = new Metadata();
		metadata.set(RESOURCE_NAME_KEY, file.getOriginalFilename());

		try (TikaInputStream stream = TikaInputStream.get(file.getInputStream())) {
			return contentTypeDetector.detect(stream, metadata);
		} catch (IOException e) {
			fileLogger().error("Failed to extract media type.", e);
			throw new FileValidationException("Failed to extract media type.");
		}
	}

	@SneakyThrows
	public MimeType extractMediaType(FileDetail fileDetail) {
		Metadata metadata = new Metadata();
		metadata.set(RESOURCE_NAME_KEY, fileDetail.fileName().value());

		try (TikaInputStream stream = TikaInputStream.get(fileDetail.fileContent()::getInputStream)) {
			MediaType mediaType = contentTypeDetector.detect(stream, metadata);
			return MimeType.of(mediaType.getBaseType().toString());
		} catch (IOException e) {
			fileLogger().error("Failed to extract media type.", e);
			throw new FileValidationException("Failed to extract media type.");
		}
	}

}
