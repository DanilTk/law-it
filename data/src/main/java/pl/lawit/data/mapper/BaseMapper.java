package pl.lawit.data.mapper;

import io.vavr.control.Option;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.kernel.util.TimeProvider;
import pl.lawit.kernel.util.UuidProvider;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Component
public class BaseMapper {

	@Autowired
	protected TimeProvider timeProvider;

	@Autowired
	protected UuidProvider uuidProvider;

	protected UUID extractUserUuid(ApplicationUserEntity entity) {
		return entity != null ? entity.getUuid() : null;
	}

	public static <T> Option<T> ofOption(T value) {
		return Option.of(value);
	}

	@SneakyThrows
	public static URL ofUrl(String url) {
		return new URI(url).toURL();
	}

}
