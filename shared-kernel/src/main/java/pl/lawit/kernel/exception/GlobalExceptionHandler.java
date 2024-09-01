package pl.lawit.kernel.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lawit.kernel.dto.BaseErrorResponseDto;
import pl.lawit.kernel.util.UuidProvider;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String CAUGHT_EXCEPTION = "Caught exception: [";

	private final UuidProvider uuidProvider;

	private final BaseErrorResponseDtoFactory baseErrorResponseDtoFactory;

	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public BaseErrorResponseDto handleAnyException(Exception exception) {
		UUID uuid = logException(exception);
		return baseErrorResponseDtoFactory.createUnknown(uuid);
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public BaseErrorResponseDto handleException(BusinessException exception) {
		UUID uuid = logException(exception);
		return baseErrorResponseDtoFactory.createBusinessError(exception, uuid);
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public BaseErrorResponseDto handleException(IllegalArgumentException exception) {
		UUID uuid = logException(exception);
		return baseErrorResponseDtoFactory.createInvalidInputError(uuid);
	}

	@ResponseStatus(UNAUTHORIZED)
	@ExceptionHandler({AccessDeniedException.class, NoPermissionException.class})
	public BaseErrorResponseDto handleException(AccessDeniedException exception) {
		UUID uuid = logException(exception);
		return baseErrorResponseDtoFactory.createAccessDenied(uuid);
	}

	@ResponseStatus(NOT_FOUND)
	@ExceptionHandler(ObjectNotFoundException.class)
	public BaseErrorResponseDto handleException(ObjectNotFoundException exception) {
		UUID uuid = logException(exception);
		return baseErrorResponseDtoFactory.createObjectNotFound(exception, uuid);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
															 HttpStatusCode statusCode, WebRequest request) {
		log.error("Caught Spring exception:", exception);
		ResponseEntity<Object> defaultResponse = super.handleExceptionInternal(
			exception,
			body,
			headers,
			statusCode,
			request
		);
		Object responseBody = createBodyForSpringException(exception);
		return new ResponseEntity<>(responseBody, defaultResponse.getHeaders(), defaultResponse.getStatusCode());
	}

	private Object createBodyForSpringException(Exception exception) {
		UUID uuid = logException(exception);

//		if (exception instanceof MissingRequestValueException ex) {
//			return baseErrorResponseDtoFactory.createSpringError(ex, uuid);
//		} else

		if (exception instanceof MaxUploadSizeExceededException ex) {
			return baseErrorResponseDtoFactory.createFileSizeError(ex, uuid);
		} else if (exception instanceof HttpMessageNotReadableException) {
			return baseErrorResponseDtoFactory.createInvalidInputError(uuid);
		}

		return baseErrorResponseDtoFactory.createUnknown(uuid);
	}

	private UUID logException(Exception exception) {
		UUID uuid = uuidProvider.getUuid();
		log.error("{}{}]\n\t", CAUGHT_EXCEPTION, uuid, exception);
		return uuid;
	}

}
