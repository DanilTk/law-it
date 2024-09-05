package pl.lawit.web.mapper;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.model.Language;
import pl.lawit.domain.model.TemplateCategory;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;

import java.util.UUID;

import static pl.lawit.domain.command.DocumentTemplateCommand.CreateDocumentTemplate;
import static pl.lawit.domain.command.DocumentTemplateCommand.CreateTemplatedDocument;
import static pl.lawit.domain.command.DocumentTemplateCommand.FindTemplates;
import static pl.lawit.web.dto.TemplateDto.CreateTemplateRequestDto;
import static pl.lawit.web.dto.TemplateDto.FindTemplatesRequestDto;
import static pl.lawit.web.dto.TemplateDto.GenerateTemplatedDocumentRequestDto;

@Component
@RequiredArgsConstructor
public class TemplateCommandMapper {

	private final AuthenticatedUserResolver authenticatedUserResolver;

	public CreateDocumentTemplate mapToCreateDocumentTemplateCommand(CreateTemplateRequestDto dto) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		return CreateDocumentTemplate.builder()
			.fileUuid(dto.fileId())
			.fileName(dto.templateName())
			.templateCategory(dto.templateCategory())
			.languageCode(dto.languageCode())
			.authenticatedUser(authenticatedUser)
			.build();
	}

	public CreateTemplatedDocument mapToCreateTemplatedDocumentCommand(UUID uuid,
																	   GenerateTemplatedDocumentRequestDto dto) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		return CreateTemplatedDocument.builder()
			.templateParameters(HashMap.ofAll(dto.data()))
			.templateUuid(uuid)
			.authenticatedUser(authenticatedUser)
			.build();
	}

	public FindTemplates mapToFindTemplatesCommand(FindTemplatesRequestDto dto) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		Option<Set<Language>> languages = Option.of(dto.getLanguages())
			.filter(set -> !set.isEmpty())
			.map(HashSet::ofAll);

		Option<Set<TemplateCategory>> categories = Option.of(dto.getTemplateCategories())
			.filter(set -> !set.isEmpty())
			.map(HashSet::ofAll);

		return FindTemplates.builder()
			.templateName(Option.of(dto.getTemplateName()))
			.languages(languages)
			.templateCategories(categories)
			.authenticatedUser(authenticatedUser)
			.build();
	}
}
