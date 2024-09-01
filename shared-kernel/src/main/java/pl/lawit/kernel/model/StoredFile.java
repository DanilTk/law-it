package pl.lawit.kernel.model;

import lombok.NonNull;

import java.net.URL;

public record StoredFile(

	@NonNull
	FilePath filePath,

	@NonNull
	URL url

) {

}
