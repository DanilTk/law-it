package pl.lawit.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.lawit.kernel.exception.FileValidationException;

import java.io.IOException;

import static org.apache.tika.metadata.TikaCoreProperties.RESOURCE_NAME_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileValidator {

	private final Detector contentTypeDetector;

	public void validate(MultipartFile file) {
		MediaType mediaType = extractMediaType(file);

		if (file.isEmpty()) {
			throw new FileValidationException("File is empty.");
		}

		if (StringUtils.isBlank(file.getContentType())) {
			throw new FileValidationException("Content type is blank.");
		}

		if (StringUtils.isBlank(file.getOriginalFilename())) {
			throw new FileValidationException("Filename is blank.");
		}

	}

	@SneakyThrows
	private MediaType extractMediaType(MultipartFile file) {
		Metadata metadata = new Metadata();
		metadata.set(RESOURCE_NAME_KEY, file.getOriginalFilename());

		try (TikaInputStream stream = TikaInputStream.get(file.getInputStream())) {
			return contentTypeDetector.detect(stream, metadata);
		} catch (IOException e) {
			log.error("Failed to extract media type.", e);
			throw new FileValidationException("Failed to extract media type.");
		}
	}

}
