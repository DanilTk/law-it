package pl.lawit.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.lawit.domain.repository.LawyerRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LawyerService {

	private final LawyerRepository lawyerRepository;

}
