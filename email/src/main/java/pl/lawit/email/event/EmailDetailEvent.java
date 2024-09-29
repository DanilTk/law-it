package pl.lawit.email.event;

import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.kernel.model.Attachment;
import pl.lawit.kernel.model.EmailAddress;

@Builder(toBuilder = true)
public record EmailDetailEvent(

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
