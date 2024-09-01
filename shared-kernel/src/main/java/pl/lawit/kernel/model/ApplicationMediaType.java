package pl.lawit.kernel.model;

import lombok.Getter;

@Getter
public enum ApplicationMediaType {
	MS_WORD_DOC("application/msword"),
	MS_WORD_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
	MS_EXCEL_XLS("application/vnd.ms-excel"),
	MS_EXCEL_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	PDF("application/pdf"),
	CSV("text/csv"),
	JPEG("image/jpeg"),
	PNG("image/png");

	private final String mediaType;

	ApplicationMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

}
