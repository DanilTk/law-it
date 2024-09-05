package pl.lawit.kernel.model;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@RequiredArgsConstructor(staticName = "of")
public class ByteArrayFileContent implements FileContent {

	private final byte[] content;

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(content);
	}

}
