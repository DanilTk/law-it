package pl.lawit.gateway.dto;

import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

@Builder(toBuilder = true)
public record OrderRequestDto(

	@NonNull
	URL notifyUrl,

	@NonNull
	String customerIp,

	@NonNull
	String merchantPosId,

	@NonNull
	String description,

	@NonNull
	String currencyCode,

	@NonNull
	BigDecimal totalAmount,

	@NonNull
	BuyerRequestDto buyer,

	@NonNull
	List<ProductRequestDto> products

) {

}
