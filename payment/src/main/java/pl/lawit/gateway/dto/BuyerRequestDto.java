package pl.lawit.gateway.dto;

import lombok.NonNull;

import java.util.UUID;

public record BuyerRequestDto(

	@NonNull
	UUID userUuid

) {
}
