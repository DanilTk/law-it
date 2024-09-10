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
@Table(name = "company")
public class CompanyEntity extends BaseEntity {

	@Size(max = 50)
	@NonNull
	@Column(name = "company_name", nullable = false, length = 50)
	private String companyName;

	@Size(max = 50)
	@NonNull
	@Column(name = "company_email", nullable = false, unique = true, length = 50)
	private String companyEmail;

	@Size(max = 12)
	@NonNull
	@Column(name = "nip", nullable = false, length = 12)
	private String nip;

}