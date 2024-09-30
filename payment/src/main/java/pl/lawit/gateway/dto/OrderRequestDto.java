package pl.lawit.gateway.dto;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder(toBuilder = true)
public record OrderRequestDto(
        @NonNull
        String notifyUrl,

        @NonNull
        String customerIp,

        @NonNull
        String merchantPosId,

        @NonNull
        String description,

        @NonNull
        String currencyCode,

        @NonNull
        String totalAmount,

        @NonNull
        BuyerDto buyer,

        @NonNull
        List<ProductRequestDto>products
) {

}
