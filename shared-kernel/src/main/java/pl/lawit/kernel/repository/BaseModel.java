package pl.lawit.kernel.repository;

import io.vavr.control.Option;

import java.time.Instant;
import java.util.UUID;

public interface BaseModel {

	UUID uuid();

	Instant createdAt();

	Option<Instant> updatedAt();

	UUID createdBy();

	Option<UUID> updatedBy();

}
