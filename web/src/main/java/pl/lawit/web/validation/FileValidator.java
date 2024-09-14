package pl.lawit.web.validation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.lawit.kernel.exception.FileValidationException;

@Component
@RequiredArgsConstructor
public class FileValidator {

	public void validate(MultipartFile file) {

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

}
