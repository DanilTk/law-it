package pl.lawit.kernel.model;

import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record EmailDetail(

	@NonNull
	String subject,

	@NonNull
	Set<EmailAddress> recipients,

	@NonNull
	Option<String> htmlBody,

	@NonNull
	Option<Attachment> attachment

) {
}
