package pl.lawit.web.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.service.CaseService;

@Component
@RequiredArgsConstructor
public class CaseHandler {

	private final CaseService caseService;

}
