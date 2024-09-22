package pl.lawit.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lawit.domain.repository.CaseRepository;

@Service
@RequiredArgsConstructor
public class CaseService {

	private final CaseRepository caseRepository;

}
