package pl.lawit.web.handler;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.command.DocumentTemplateCommand;
import pl.lawit.domain.command.DocumentTemplateCommand.CreateDocumentTemplate;
import pl.lawit.domain.command.DocumentTemplateCommand.FindTemplates;
import pl.lawit.domain.model.DocumentTemplate;
import pl.lawit.domain.model.TemplateCategory;
import pl.lawit.domain.service.TemplateService;
import pl.lawit.kernel.model.FileDetail;
import pl.lawit.web.dto.ListResponseDto;
import pl.lawit.web.dto.TemplateDto.FindTemplatesRequestDto;
import pl.lawit.web.dto.TemplateDto.TemplateResponseDto;
import pl.lawit.web.mapper.TemplateCommandMapper;
import pl.lawit.web.mapper.TemplateDtoMapper;

import java.util.UUID;

import static pl.lawit.web.dto.TemplateDto.CreateTemplateRequestDto;
import static pl.lawit.web.dto.TemplateDto.GenerateTemplatedDocumentRequestDto;

@Component
@RequiredArgsConstructor
public class TemplateHandler {

	private final TemplateService templateService;

	private final TemplateCommandMapper commandMapper;

	public TemplateResponseDto createTemplate(CreateTemplateRequestDto dto) {
		CreateDocumentTemplate command = commandMapper.mapToCreateDocumentTemplateCommand(dto);
		DocumentTemplate documentTemplate = templateService.createTemplate(command);
		return TemplateDtoMapper.map(documentTemplate);
	}

	public FileDetail generateDocument(UUID uuid, GenerateTemplatedDocumentRequestDto dto) {
		DocumentTemplateCommand.CreateTemplatedDocument command =
			commandMapper.mapToCreateTemplatedDocumentCommand(uuid, dto);
		return templateService.generateTemplatedDocument(command);
	}

	public ListResponseDto<TemplateResponseDto> findTemplates(FindTemplatesRequestDto dto) {
		FindTemplates command = commandMapper.mapToFindTemplatesCommand(dto);
		List<DocumentTemplate> all = templateService.findTemplates(command);
		return ListResponseDto.of(all.map(TemplateDtoMapper::map));
	}

	public ListResponseDto<TemplateCategory> findAllCategories() {
		return ListResponseDto.of(List.of(TemplateCategory.values()));
	}
}
