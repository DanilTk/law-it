package pl.lawit.gateway.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.gateway.configuration.PaymentGatewayPayUProperties;
import pl.lawit.gateway.dto.BuyerRequestDto;
import pl.lawit.gateway.dto.OrderRequestDto;
import pl.lawit.gateway.dto.ProductRequestDto;

import java.math.BigDecimal;
import java.util.List;

import static pl.lawit.domain.command.PaymentOrderCommand.PlacePaymentOrder;

@Component
@RequiredArgsConstructor
public class PayURequestFactory {

	private final PaymentGatewayPayUProperties paymentGatewayPayuProperties;

	public OrderRequestDto getOrderRequestDto(PlacePaymentOrder command) {
		BuyerRequestDto buyer = new BuyerRequestDto(command.authenticatedUser().userUuid());

		String productDescription = String.format("Legal service: %s", command.legalCase().descriptionUuid());

		ProductRequestDto product = ProductRequestDto.builder()
			.name(productDescription)
			.unitPrice(command.price().multiply(BigDecimal.valueOf(100)).toString())
			.quantity(String.valueOf(1))
			.build();

		List<ProductRequestDto> productRequestDtoList = List.of(product);

		return OrderRequestDto.builder()
			.notifyUrl(paymentGatewayPayuProperties.getCallbackUrl())
			.customerIp(command.clientIP())
			.merchantPosId(paymentGatewayPayuProperties.getClientId())
			.currencyCode(command.currencyCode().name())
			.totalAmount(command.price().multiply(BigDecimal.valueOf(100)).toString())
			.description(command.legalCase().descriptionUuid().toString())
			.products(productRequestDtoList)
			.buyer(buyer)
			.build();
	}

}
