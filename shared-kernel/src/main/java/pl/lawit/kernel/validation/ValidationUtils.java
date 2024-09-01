package pl.lawit.kernel.validation;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

@UtilityClass
public class ValidationUtils {

	public static void require(boolean condition, String message) {
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void requireNotNull(@Nullable Object value, String message) {
		require(value != null, message);
	}

	public static void requireNotNull(@Nullable Object value) {
		requireNotNull(value, "value cannot be null");
	}

	public static void requireNotBlank(@Nullable String value, String message) {
		require(StringUtils.isNotBlank(value), message);
	}

	public static void requireNotBlank(@Nullable String value) {
		requireNotBlank(value, "value cannot be empty");
	}

	public static void check(boolean condition, String message) {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}

}
