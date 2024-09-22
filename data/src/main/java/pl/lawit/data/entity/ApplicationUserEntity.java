package pl.lawit.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED, force = true)
@Table(name = "application_user")
public class ApplicationUserEntity {

	@Id
	@Column(name = "uuid", nullable = false)
	@NonNull
	private UUID uuid;

	@Version
	@Column(name = "version", nullable = false)
	@Setter
	private Integer version;

	@Column(name = "created_at", nullable = false)
	@Setter
	@CreationTimestamp
	@NonNull
	private Instant createdAt;

	@Column(name = "last_sync_at")
	@Nullable
	private Instant lastSyncAt;

	@Column(name = "idp_sub", nullable = false, unique = true)
	@NonNull
	private String idpUid;

	@Column(name = "email", unique = true)
	@NonNull
	private String email;

	@Column(name = "is_idp_user", nullable = false)
	@NonNull
	private Boolean isIdpUser;

	@Column(name = "last_login_at")
	@Nullable
	private Instant lastLoginAt;

}