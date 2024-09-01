package pl.lawit.kernel.validation;

import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record Violation(

	@NonNull
	ViolationSubject subject,

	@NonNull
	ViolationCause cause,

	@NonNull
	String violationMessage

) {

}
