package pl.lawit.web.handler;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.domain.processor.UserSyncProcessor;
import pl.lawit.domain.service.UserService;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.web.dto.PageResponseDto;
import pl.lawit.web.dto.PageableRequestDto;
import pl.lawit.web.dto.UserDto.UserResponseDto;
import pl.lawit.web.factory.PageResponseDtoFactory;
import pl.lawit.web.mapper.PageCommandMapper;
import pl.lawit.web.mapper.UserDtoMapper;

@Component
@RequiredArgsConstructor
public class UserHandler {

	private final PageResponseDtoFactory pageResponseDtoFactory;

	private final UserService userService;

	private final UserSyncProcessor userSyncProcessor;

	public PageResponseDto<UserResponseDto> findUsers(PageableRequestDto dto) {
		PageCommandQuery commandQuery = PageCommandMapper.map(dto);
		PageResult<ApplicationUser> page = userService.findAll(commandQuery);
		return pageResponseDtoFactory.create(page, UserDtoMapper::map);
	}

	public Option<UserResponseDto> findByEmail(String email) {
		EmailAddress emailAddress = EmailAddress.of(email);
		Option<ApplicationUser> applicationUser = userService.findByEmail(emailAddress);
		return applicationUser.map(UserDtoMapper::map);
	}

	public void synchronizeUsers() {
		userSyncProcessor.synchronizeUsers();
	}

}
