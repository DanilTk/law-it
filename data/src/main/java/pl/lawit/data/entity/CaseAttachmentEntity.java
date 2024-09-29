package pl.lawit.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@SuperBuilder
@Entity
@Table(name = "case_attachment")
public class CaseAttachmentEntity extends BaseEntity {

	@NonNull
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "case_uuid", nullable = false)
	private LegalCaseEntity legalCase;

	@NonNull
	@OneToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "file_uuid", nullable = false)
	private RegisteredFileEntity file;

}