package pl.lawit.data.mapper;

import io.vavr.control.Option;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.kernel.model.MoneyAmount;
import pl.lawit.kernel.util.TimeProvider;
import pl.lawit.kernel.util.UuidProvider;

import java.math.BigDecimal;
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
	public static URL ofUrl(String value) {
		return new URI(value).toURL();
	}

	@SneakyThrows
	public static MoneyAmount ofMoneyAmount(BigDecimal value) {
		return MoneyAmount.of(value);
	}

}
