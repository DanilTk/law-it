package pl.lawit.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@MappedSuperclass
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
public abstract class BaseEntity implements Serializable {

	@Id
	@Column(name = "uuid", nullable = false)
	@Setter
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

	@Column(name = "updated_at")
	@Setter
	@UpdateTimestamp
	@Nullable
	private Instant updatedAt;

	@Setter
	@NonNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "created_by", nullable = false)
	private ApplicationUserEntity createdBy;

	@Setter
	@Nullable
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "updated_by")
	private ApplicationUserEntity updatedBy;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof BaseEntity that)) {
			return false;
		}
		return Objects.equals(uuid, that.uuid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

}
