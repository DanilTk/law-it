package pl.lawit.kernel.repository;

import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.UUID;

public interface BaseRepository<T extends BaseModel> {

	T getByUuid(UUID uuid);

	Option<T> findByUuid(UUID uuid);

	List<T> findAll();

}
