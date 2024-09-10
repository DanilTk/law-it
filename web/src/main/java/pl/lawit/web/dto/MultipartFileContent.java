package pl.lawit.web.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pl.lawit.kernel.model.FileContent;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor(staticName = "of")
public class MultipartFileContent implements FileContent {

	@NonNull
	private final MultipartFile multipartFile;

	@Override
	public InputStream getInputStream() throws IOException {
		return multipartFile.getInputStream();
	}

}
