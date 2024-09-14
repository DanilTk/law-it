package pl.lawit.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.processor.EmailProcessor;

@Component
@RequiredArgsConstructor
public class EmailDetailProcessor implements EmailProcessor {

	private final EmailClient emailClient;

}
