package pl.lawit.web.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.command.LawyerCommand.CreateLawyer;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Lawyer;
import pl.lawit.domain.service.LawyerService;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.web.dto.LawyerDto.CreateLawyerRequestDto;
import pl.lawit.web.dto.LawyerDto.LawyerResponseDto;
import pl.lawit.web.dto.PageResponseDto;
import pl.lawit.web.dto.PageableRequestDto;
import pl.lawit.web.factory.PageResponseDtoFactory;
import pl.lawit.web.mapper.LawyerCommandMapper;
import pl.lawit.web.mapper.LawyerDtoMapper;
import pl.lawit.web.mapper.PageCommandMapper;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LawyerHandler {

	private final LawyerCommandMapper lawyerCommandMapper;

	private final PageResponseDtoFactory pageResponseDtoFactory;

	private final LawyerService lawyerService;

	public LawyerResponseDto createLawyer(CreateLawyerRequestDto dto) {
		CreateLawyer command = lawyerCommandMapper.mapToCreateLawyerCommand(dto);
		Lawyer lawyer = lawyerService.createLawyer(command);
		return LawyerDtoMapper.map(lawyer);
	}

	public LawyerResponseDto getLawyerByUuid(UUID uuid) {
		Lawyer lawyer = lawyerService.getLawyer(uuid);
		return LawyerDtoMapper.map(lawyer);
	}

	public PageResponseDto<LawyerResponseDto> findLawyersPage(PageableRequestDto dto) {
		PageCommandQuery commandQuery = PageCommandMapper.map(dto);
		PageResult<Lawyer> page = lawyerService.findLawyersPage(commandQuery);
		return pageResponseDtoFactory.create(page, LawyerDtoMapper::map);
	}
}
