package pl.lawit.email;

import io.vavr.collection.Set;
import io.vavr.control.Try;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.model.Attachment;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.EmailDetail;

import static pl.lawit.kernel.logger.ApplicationLoggerFactory.emailLogger;

@Component
@RequiredArgsConstructor
public class EmailClient {

	private static final String PLACEHOLDER = "";

	private final JavaMailSender sender = null;//todo:fix

	@Value("${application.email.enabled:false}")
	private boolean isEmailClientEnabled;

	@Value("${application.email.outbound-email:null}")
	private String outboundEmail;

	@Async
	public void sendEmail(EmailDetail emailDetail) {
		if (isEmailClientEnabled) {
			Try.run(() -> {
					MimeMessage message;
					String subject = emailDetail.subject();
					if (emailDetail.htmlBody().isDefined()) {
						message = prepareMime(emailDetail.recipients(), subject, emailDetail.htmlBody().get());
					} else {
						message = prepareMime(emailDetail.recipients(), subject, emailDetail.attachment().get());
					}

					sender.send(message);
				})
				.onFailure($ -> emailLogger().error("Failed to send an email.", $))
				.onSuccess($ -> emailLogger().info("Email with subject: {} sent from: {} to: {}",
					emailDetail.subject(), outboundEmail,
					emailDetail.recipients().map(EmailAddress::value).toJavaArray()));
		}
	}

	@SneakyThrows
	private MimeMessage prepareMime(Set<EmailAddress> recipients, String subject, Attachment attachment) {
		String[] recipientsAddressList = recipients.map(EmailAddress::value)
			.toJavaList()
			.toArray(String[]::new);

		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		message.setFrom(new InternetAddress(outboundEmail, "Application"));
		helper.setBcc(recipientsAddressList);
		helper.setSubject(EmailSubject.valueOf(subject).getSubject());
		helper.addAttachment(attachment.fileName(), new ByteArrayResource(attachment.content()));
		helper.setText(PLACEHOLDER, true);

		return message;
	}

	@SneakyThrows
	private MimeMessage prepareMime(Set<EmailAddress> recipients, String subject, String htmlBody) {
		String[] recipientsAddressList = recipients.map(EmailAddress::value)
			.toJavaList()
			.toArray(String[]::new);

		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		message.setFrom(new InternetAddress(outboundEmail, "Application"));
		helper.setBcc(recipientsAddressList);
		helper.setSubject(EmailSubject.valueOf(subject).getSubject());
		helper.setText(htmlBody, true);

		return message;
	}
}
