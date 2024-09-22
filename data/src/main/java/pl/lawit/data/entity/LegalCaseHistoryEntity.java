package pl.lawit.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;
import pl.lawit.domain.model.CaseStatus;

import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@SuperBuilder
@Entity
@Table(name = "legal_case_history")
public class LegalCaseHistoryEntity extends BaseEntity {

	@NonNull
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "case_uuid", nullable = false)
	private LegalCaseEntity legalCase;

	@NonNull
	@Enumerated(STRING)
	@Column(name = "case_status", nullable = false)
	private CaseStatus caseStatus;

	@Nullable
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "lawyer_uuid")
	private LawyerEntity lawyer;

	@Nullable
	@Column(name = "acceptance_deadline")
	private Instant acceptanceDeadline;

	@Nullable
	@Column(name = "completion_deadline")
	private Instant completionDeadline;

}