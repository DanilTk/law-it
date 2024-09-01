package pl.lawit.email;

import lombok.Getter;

@Getter
public enum EmailSubject {
	NONE("NONE");

	private final String subject;

	EmailSubject(String subject) {
		this.subject = subject;
	}

}