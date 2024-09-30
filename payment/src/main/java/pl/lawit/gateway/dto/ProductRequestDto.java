package pl.lawit.gateway.dto;

import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record ProductRequestDto(

	@NonNull
	String name,

	@NonNull
	BigDecimal unitPrice

) {
}