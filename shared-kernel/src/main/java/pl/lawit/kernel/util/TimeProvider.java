package pl.lawit.kernel.util;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimeProvider {

	public Instant getInstant() {
		return Instant.now();
	}

}
