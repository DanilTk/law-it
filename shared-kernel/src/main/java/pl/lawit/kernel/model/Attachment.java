package pl.lawit.kernel.model;

import lombok.NonNull;

public record Attachment(

	@NonNull
	String fileName,

	byte[] content

) {
}
