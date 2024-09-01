package pl.lawit.kernel.repository;

import java.time.Instant;
import java.util.UUID;

public interface BaseModel {

	UUID uuid();

	Instant createdAt();

	Instant updatedAt();

	UUID createdBy();

	UUID updatedBy();

}
