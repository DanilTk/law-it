package pl.lawit.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.model.FileName;
import pl.lawit.kernel.model.FilePath;
import pl.lawit.kernel.util.UuidProvider;

@Component
@RequiredArgsConstructor
public class FilePathProvider {

	private final UuidProvider uuidProvider;

	public FilePath createBlobFilePath(FileName fileName) {
		String uniqueFolder = uuidProvider.getUuid() + "/";
		String fullPath = uniqueFolder + fileName.value();

		return FilePath.of(fullPath);
	}
}
