package pl.lawit.kernel.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidProvider {

	public UUID getUuid() {
		return UUID.randomUUID();
	}

}
