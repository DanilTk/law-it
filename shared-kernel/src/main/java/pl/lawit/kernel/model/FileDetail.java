package pl.lawit.kernel.model;

import lombok.Builder;
import org.springframework.core.io.ByteArrayResource;

@Builder(toBuilder = true)
public record FileDetail(

	FileName fileName,

	ByteArrayResource fileContent

) {
}
