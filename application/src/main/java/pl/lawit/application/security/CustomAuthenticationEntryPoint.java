package pl.lawit.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.dto.BaseErrorResponseDto;
import pl.lawit.kernel.util.UuidProvider;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final UuidProvider uuidProvider;

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws IOException {

		BaseErrorResponseDto errorResponse = BaseErrorResponseDto.builder()
			.statusCode(UNAUTHORIZED.value())
			.message("Unauthorized access")
			.errorId(uuidProvider.getUuid())
			.build();

		response.setStatus(UNAUTHORIZED.value());
		response.setContentType(APPLICATION_JSON_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
