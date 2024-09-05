package pl.lawit.kernel.authentication;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record ApplicationUserDetails(

	UUID uuid,

	String sub

) {
}
