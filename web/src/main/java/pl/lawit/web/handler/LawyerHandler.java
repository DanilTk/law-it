package pl.lawit.web.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.service.LawyerService;

@Component
@RequiredArgsConstructor
public class LawyerHandler {

	private final LawyerService lawyerService;

}
