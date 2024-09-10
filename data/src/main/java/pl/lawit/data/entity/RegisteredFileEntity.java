package pl.lawit.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@SuperBuilder
@Entity
@Table(name = "registered_file")
public class RegisteredFileEntity extends BaseEntity {

	@Column(name = "mime_type", nullable = false)
	@NonNull
	private String mimeType;

	@Column(name = "original_file_name", nullable = false)
	@NonNull
	private String originalFileName;

	@Column(name = "file_path", nullable = false)
	@NonNull
	private String filePath;

	@Column(name = "file_url", nullable = false)
	@NonNull
	private String fileUrl;

	@Column(name = "file_size", nullable = false)
	@NonNull
	private Long fileSize;

	@Column(name = "md5_checksum", nullable = false, length = 32)
	@Size(max = 32)
	@NonNull
	private String md5Checksum;

}