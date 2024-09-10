package pl.lawit.kernel.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class DateTimeUtil {

	public static Instant toInstant(long epochSecond) {
		return Instant.ofEpochSecond(epochSecond / 1000);
	}

}
