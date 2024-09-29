package pl.lawit.gateway;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.lawit.domain.model.CurrencyCode;
import pl.lawit.domain.model.PaymentOrderDetail;
import pl.lawit.domain.processor.PaymentOrderProcessor;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class PayUPaymentOrderProcessor implements PaymentOrderProcessor {

	@Override
	@SneakyThrows
	public PaymentOrderDetail placePaymentOrder() {
		Faker faker = new Faker();
		String url = faker.internet().url();
		URI uri = URI.create(url);

		return PaymentOrderDetail.builder()
			.currencyCode(CurrencyCode.PLN)
			.paymentLink(uri.toURL())
			.orderId(faker.bothify("extOrder-####"))
			.build();
	}

	@Override
	public String refundPayment() {
		Faker faker = new Faker();
		return faker.bothify("extOrder-####");
	}

}
