package pl.lawit.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.lawit.domain.model.Language;
import pl.lawit.domain.model.TemplateCategory;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@SuperBuilder
@Entity
@Table(name = "document_template")
public class DocumentTemplateEntity extends BaseEntity {

	@NonNull
	@Enumerated(STRING)
	@Column(name = "template_category", nullable = false)
	private TemplateCategory templateCategory;

	@NonNull
	@Enumerated(STRING)
	@Column(name = "language_code", nullable = false)
	private Language languageCode;

	@Size(min = 1, max = 50)
	@NonNull
	@Column(name = "template_name", nullable = false, length = 50)
	private String templateName;

	@NonNull
	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "file_uuid")
	private RegisteredFileEntity file;

}