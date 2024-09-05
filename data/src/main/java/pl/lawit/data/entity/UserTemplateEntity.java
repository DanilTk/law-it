package pl.lawit.data.entity;

import jakarta.persistence.Entity;
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

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@SuperBuilder
@Entity
@Table(name = "user_template")
public class UserTemplateEntity extends BaseEntity {

	@Nullable
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "company_uuid")
	private CompanyEntity company;

	@NonNull
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "template_uuid")
	private DocumentTemplateEntity documentTemplate;

}