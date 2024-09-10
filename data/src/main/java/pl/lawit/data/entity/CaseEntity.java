package pl.lawit.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;
import pl.lawit.domain.model.CaseStatus;
import pl.lawit.domain.model.CaseType;

import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@SuperBuilder
@Entity
@Table(name = "\"case\"")
public class CaseEntity extends BaseEntity {

	@Size(max = 100)
	@NonNull
	@Column(name = "case_title", nullable = false, length = 100)
	private String title;

	@NonNull
	@Column(name = "description_uuid", nullable = false)
	private UUID descriptionUuid;

	@NonNull
	@Enumerated(STRING)
	@Column(name = "case_type", nullable = false)
	private CaseType caseType;

	@NonNull
	@Enumerated(STRING)
	@Column(name = "case_status", nullable = false)
	private CaseStatus caseStatus;

	@Nullable
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "lawyer_uuid")
	private LawyerEntity lawyer;

	@Nullable
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "company_uuid")
	private CompanyEntity company;

}