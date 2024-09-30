package pl.lawit.gateway;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.lawit.domain.model.LegalCase;
import pl.lawit.domain.model.PaymentResponse;
import pl.lawit.domain.processor.PaymentOrderProcessor;
import pl.lawit.gateway.configuration.PaymentGatewayPayuConfig;
import pl.lawit.gateway.dto.BuyerDto;
import pl.lawit.gateway.dto.OrderRequestDto;
import pl.lawit.gateway.dto.ProductRequestDto;
import pl.lawit.gateway.service.PayUAuthorizationService;
import pl.lawit.kernel.authentication.AuthenticatedUser;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayUPaymentOrderProcessor implements PaymentOrderProcessor {

	private static String ORDER_SUFFIX_URL = "/api/v2_1/orders";

	private final RestClient restClient;

	private final PayUAuthorizationService payUAuthorizationService;

	private final PaymentGatewayPayuConfig paymentGatewayPayuConfig;

	@Override
	public String refundPayment() {
		Faker faker = new Faker();
		return faker.bothify("extOrder-####");
	}

	@Override
	public PaymentResponse placePaymentOrder(LegalCase legalCase, AuthenticatedUser authenticatedUser,
											 String ipAddress) {

		BuyerDto buyer = new BuyerDto(authenticatedUser.userUuid());
		ProductRequestDto product =  ProductRequestDto.builder()
			.name("Consultation Service")
			.unitPrice(BigDecimal.ONE)//todo: price to comve from calling method
			.build();

		List<ProductRequestDto> productRequestDtoList = List.of(product);

		OrderRequestDto orderRequestDto = OrderRequestDto.builder()
			.notifyUrl("https://webhook.site/d3452338-be41-43da-89f3-4018b1ff5d21") //todo: move to yaml
			.customerIp(ipAddress)
			.merchantPosId(payUAuthorizationService.getClientId())
			.currencyCode("PLN")
			.totalAmount("150")
			.description(legalCase.descriptionUuid().toString())
			.products(productRequestDtoList)
			.buyer(buyer)
			.build();

		payUAuthorizationService.generateAccessToken();

		URI uri = URI.create(paymentGatewayPayuConfig.getBaseUrl() + ORDER_SUFFIX_URL);

		ResponseEntity<PaymentResponse> response = restClient
			.post()
			.uri(uri)
			.header(AUTHORIZATION, "Bearer " + payUAuthorizationService.getBearerToken().get())
			.body(orderRequestDto)
			.retrieve()
			.toEntity(PaymentResponse.class);

		return response.getBody();
	}

}
