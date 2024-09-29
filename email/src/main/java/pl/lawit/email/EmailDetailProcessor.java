package pl.lawit.email;

import io.vavr.collection.HashSet;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.processor.EmailProcessor;
import pl.lawit.email.event.EmailDetailEvent;

@Component
@RequiredArgsConstructor
public class EmailDetailProcessor implements EmailProcessor {

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	@Transactional
	public void sendEmail() {
		EmailDetailEvent event = EmailDetailEvent.builder()
			.subject("Email subject")
			.recipients(HashSet.empty())
			.htmlBody(Option.none())
			.attachment(Option.none())
			.build();

		applicationEventPublisher.publishEvent(event);
	}
}
