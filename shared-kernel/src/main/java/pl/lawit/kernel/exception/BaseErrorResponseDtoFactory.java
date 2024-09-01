package pl.lawit.kernel.exception;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import pl.lawit.kernel.dto.BaseErrorResponseDto;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class BaseErrorResponseDtoFactory {

	@Value("${spring.servlet.multipart.max-file-size}")
	private String allowedFileSize;

	public BaseErrorResponseDto createUnknown(UUID uuid) {
		return BaseErrorResponseDto.builder()
			.message("An error occurred. Please try again later.")
			.statusCode(INTERNAL_SERVER_ERROR.value())
			.errorId(uuid)
			.build();
	}

	BaseErrorResponseDto createRateLimitError() {
		return BaseErrorResponseDto.builder()
			.message("Too many requests, please try again later.")
			.statusCode(TOO_MANY_REQUESTS.value())
			.build();
	}

//	public BaseErrorResponseDto createSpringError(MissingRequestValueException exception, UUID uuid) {
//		String message = Option.of(exception.getMessage()).getOrElse("Exception");
//
//		return BaseErrorResponseDto.builder()
//			.message(message)
//			.statusCode(BAD_REQUEST.value())
//			.errorId(uuid)
//			.build();
//	}

	public BaseErrorResponseDto createInvalidInputError(UUID uuid) {
		return BaseErrorResponseDto.builder()
			.message("Invalid input.")
			.statusCode(BAD_REQUEST.value())
			.errorId(uuid)
			.build();
	}

	public BaseErrorResponseDto createAccessDenied(UUID uuid) {
		return BaseErrorResponseDto.builder()
			.message("Access denied.")
			.statusCode(UNAUTHORIZED.value())
			.errorId(uuid)
			.build();
	}

	public BaseErrorResponseDto createObjectNotFound(ObjectNotFoundException exception, UUID uuid) {
		return BaseErrorResponseDto.builder()
			.message(exception.getFriendlyMessage())
			.statusCode(NOT_FOUND.value())
			.errorId(uuid)
			.build();
	}

	public BaseErrorResponseDto createBusinessError(BusinessException exception, UUID uuid) {
		return BaseErrorResponseDto.builder()
			.message(exception.getFriendlyMessage())
			.statusCode(BAD_REQUEST.value())
			.errorId(uuid)
			.build();
	}

	public BaseErrorResponseDto createFileSizeError(MaxUploadSizeExceededException exception, UUID uuid) {
		String actualFileSize = StringUtils.substringBetween(exception.getMessage(), "(", ")");
		String fileSizeMb = FileUtils.byteCountToDisplaySize(Long.parseLong(actualFileSize)).replace(" ", "");
		String message = String.format("File size: %s is more than allowed limit: %s.", fileSizeMb, allowedFileSize);

		return BaseErrorResponseDto.builder()
			.message(message)
			.statusCode(PAYLOAD_TOO_LARGE.value())
			.errorId(uuid)
			.build();
	}

}
