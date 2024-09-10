package pl.lawit.kernel.authentication;

import lombok.Getter;

@Getter
public enum ClaimKey {
	ROLE_CLAIM("role");

	private final String key;

	ClaimKey(String key) {
		this.key = key;
	}
}
