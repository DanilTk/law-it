package pl.lawit.gateway.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record ProductRequestDto(

	@NonNull
	String name,

	@NonNull
	String unitPrice,

	@NonNull
	String quantity

) {
}