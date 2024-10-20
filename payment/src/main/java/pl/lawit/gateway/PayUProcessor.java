package pl.lawit.gateway;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.lawit.domain.model.PaymentResponseDto;
import pl.lawit.domain.processor.PaymentOrderProcessor;
import pl.lawit.gateway.configuration.PaymentGatewayPayUProperties;
import pl.lawit.gateway.dto.OrderRequestDto;
import pl.lawit.gateway.mapper.PayURequestFactory;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static pl.lawit.domain.command.PaymentOrderCommand.PlacePaymentOrder;

@Component
@RequiredArgsConstructor
public class PayUProcessor implements PaymentOrderProcessor {

	private static String ORDER_API_URL = "/api/v2_1/orders";

	private final RestClient restClient;

	private final PayUAuthorizationResolver payUAuthorizationResolver;

	private final PayURequestFactory payURequestFactory;

	private final PaymentGatewayPayUProperties paymentGatewayPayuProperties;

	@Override
	@SneakyThrows
	public PaymentResponseDto placePaymentOrder(PlacePaymentOrder command) {
		String token = payUAuthorizationResolver.getAccessToken();
		String requestUrl = paymentGatewayPayuProperties.getBaseUrl().toURI() + ORDER_API_URL;
		OrderRequestDto requestBody = payURequestFactory.getOrderRequestDto(command);

		ResponseEntity<PaymentResponseDto> response = restClient
			.post()
			.uri(requestUrl)
			.header(AUTHORIZATION, "Bearer " + token)
			.body(requestBody)
			.retrieve()
			.toEntity(PaymentResponseDto.class);

		return response.getBody();
	}

	@Override
	public String refundPayment() {
		Faker faker = new Faker();
		return faker.bothify("extOrder-####");
	}

}
