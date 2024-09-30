package pl.lawit.gateway.dto;

import lombok.NonNull;

import java.util.UUID;

public record BuyerDto(

	@NonNull
	UUID userUuid

) {
}
