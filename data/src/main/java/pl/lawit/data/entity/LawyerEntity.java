package pl.lawit.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@SuperBuilder
@Entity
@Table(name = "lawyer")
public class LawyerEntity extends BaseEntity {

	@NonNull
	@OneToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "user_uuid", nullable = false)
	private ApplicationUserEntity applicationUser;

	@NonNull
	@OneToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "certificate_file_uuid")
	private RegisteredFileEntity certificate;

	@Nullable
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "company_uuid")
	private CompanyEntity company;

	@Size(max = 11)
	@NonNull
	@Column(name = "pesel", nullable = false, length = 11)
	private String pesel;

	@NonNull
	@Column(name = "hourly_rate", nullable = false)
	private BigDecimal hourlyRate;

}