package pl.lawit.web.mapper;

import lombok.experimental.UtilityClass;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.web.dto.UserDto.UserResponseDto;

@UtilityClass
public final class UserDtoMapper {

	public static UserResponseDto map(ApplicationUser applicationUser) {
		return UserResponseDto.builder()
			.id(applicationUser.uuid())
			.email(applicationUser.email().value())
			.isActiveAccount(applicationUser.isIdpUser())
			.createdAt(applicationUser.createdAt())
			.build();
	}

}
