package pl.lawit.domain.service;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.command.DocumentTemplateCommand.CreateDocumentTemplate;
import pl.lawit.domain.model.DocumentTemplate;
import pl.lawit.domain.model.RegisteredFile;
import pl.lawit.domain.processor.TemplateProcessor;
import pl.lawit.domain.repository.RegisteredFileRepository;
import pl.lawit.domain.repository.TemplateRepository;
import pl.lawit.kernel.model.FileDetail;

import static pl.lawit.domain.command.DocumentTemplateCommand.CreateTemplatedDocument;
import static pl.lawit.domain.command.DocumentTemplateCommand.FindTemplates;
import static pl.lawit.domain.command.DocumentTemplateCommand.GenerateTemplatedDocument;
import static pl.lawit.kernel.model.ApplicationMediaType.PDF;

@Service
@RequiredArgsConstructor
public class TemplateService {

	private final TemplateRepository templateRepository;

	private final RegisteredFileRepository registeredFileRepository;

	private final TemplateProcessor templateProcessor;

	@Transactional
	public DocumentTemplate createTemplate(CreateDocumentTemplate command) {
		RegisteredFile registeredFile = registeredFileRepository.getByUuid(command.fileUuid());
		if (!registeredFile.mimeType().value().equals(PDF.getMediaType())) {
			throw new IllegalArgumentException("File must be a PDF");
		}

		return templateRepository.create(command);
	}

	@Transactional
	public List<DocumentTemplate> findTemplates(FindTemplates commandQuery) {
		return templateRepository.findAll(commandQuery);
	}

	@Transactional
	public FileDetail generateTemplatedDocument(CreateTemplatedDocument command) {
		DocumentTemplate template = templateRepository.getByUuid(command.templateUuid());
		RegisteredFile registeredFile = registeredFileRepository.getByUuid(template.fileUuid());

		GenerateTemplatedDocument processorCommand = GenerateTemplatedDocument.builder()
			.templateParameters(command.templateParameters())
			.templateName(template.templateName())
			.filePath(registeredFile.filePath())
			.build();

		FileDetail templatedDocument = templateProcessor.generateDocument(processorCommand);

		//store generated file using file service
		//add record to db that user generated template
		//add methods to template repository to find

		return templatedDocument;
	}

}
