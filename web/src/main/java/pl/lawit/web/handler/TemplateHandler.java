package pl.lawit.web.handler;

import io.vavr.collection.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.command.DocumentTemplateCommand;
import pl.lawit.domain.command.DocumentTemplateCommand.CreateDocumentTemplate;
import pl.lawit.domain.command.DocumentTemplateCommand.FindTemplates;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.DocumentTemplate;
import pl.lawit.domain.model.PurchasedDocumentInfo;
import pl.lawit.domain.model.TemplateCategory;
import pl.lawit.domain.service.TemplateService;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;
import pl.lawit.kernel.model.FileDetail;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.web.dto.ListResponseDto;
import pl.lawit.web.dto.PageResponseDto;
import pl.lawit.web.dto.PageableRequestDto;
import pl.lawit.web.dto.TemplateDto.FindTemplatesRequestDto;
import pl.lawit.web.dto.TemplateDto.PurchasedDocumentResponseDto;
import pl.lawit.web.dto.TemplateDto.TemplateResponseDto;
import pl.lawit.web.factory.PageResponseDtoFactory;
import pl.lawit.web.mapper.PageCommandMapper;
import pl.lawit.web.mapper.PurchasedDocumentDtoMapper;
import pl.lawit.web.mapper.TemplateCommandMapper;
import pl.lawit.web.mapper.TemplateDtoMapper;

import java.util.UUID;

import static pl.lawit.web.dto.TemplateDto.CreateTemplateRequestDto;
import static pl.lawit.web.dto.TemplateDto.GenerateTemplatedDocumentRequestDto;

@Component
@RequiredArgsConstructor
public class TemplateHandler {

	private final AuthenticatedUserResolver authenticatedUserResolver;

	private final PageResponseDtoFactory pageResponseDtoFactory;

	private final TemplateService templateService;

	private final TemplateCommandMapper commandMapper;

	public TemplateResponseDto createTemplate(CreateTemplateRequestDto dto) {
		CreateDocumentTemplate command = commandMapper.mapToCreateDocumentTemplateCommand(dto);
		DocumentTemplate documentTemplate = templateService.createTemplate(command);
		return TemplateDtoMapper.map(documentTemplate);
	}

	public FileDetail purchaseDocument(UUID uuid, GenerateTemplatedDocumentRequestDto dto) {
		DocumentTemplateCommand.CreateTemplatedDocument command = commandMapper
			.mapToCreateTemplatedDocumentCommand(uuid, dto);
		return templateService.purchaseDocument(command);
	}

	public ListResponseDto<TemplateResponseDto> findTemplates(FindTemplatesRequestDto dto) {
		FindTemplates command = commandMapper.mapToFindTemplatesCommand(dto);
		List<DocumentTemplate> all = templateService.findTemplates(command);
		return ListResponseDto.of(all.map(TemplateDtoMapper::map));
	}

	public ListResponseDto<TemplateCategory> findAllCategories() {
		return ListResponseDto.of(List.of(TemplateCategory.values()));
	}

	public PageResponseDto<PurchasedDocumentResponseDto> findUserPurchasedTemplates(@Valid PageableRequestDto dto) {
		PageCommandQuery commandQuery = PageCommandMapper.map(dto);
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();
		PageResult<PurchasedDocumentInfo> page = templateService.findUserTemplates(authenticatedUser, commandQuery);
		return pageResponseDtoFactory.create(page, PurchasedDocumentDtoMapper::map);
	}
}
