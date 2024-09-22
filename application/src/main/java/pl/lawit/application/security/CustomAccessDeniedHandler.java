package pl.lawit.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.dto.BaseErrorResponseDto;
import pl.lawit.kernel.util.UuidProvider;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final UuidProvider uuidProvider;

	private final ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException {
		BaseErrorResponseDto errorResponse = BaseErrorResponseDto.builder()
			.statusCode(FORBIDDEN.value())
			.message("Access denied")
			.errorId(uuidProvider.getUuid())
			.build();

		response.setStatus(FORBIDDEN.value());
		response.setContentType(APPLICATION_JSON_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
