package pl.lawit.domain.service;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.command.DocumentTemplateCommand.CreateDocumentTemplate;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.DocumentTemplate;
import pl.lawit.domain.model.PurchasedDocument;
import pl.lawit.domain.model.PurchasedDocumentInfo;
import pl.lawit.domain.model.RegisteredFile;
import pl.lawit.domain.processor.TemplateProcessor;
import pl.lawit.domain.repository.PurchasedDocumentRepository;
import pl.lawit.domain.repository.RegisteredFileRepository;
import pl.lawit.domain.repository.TemplateRepository;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.FileDetail;
import pl.lawit.kernel.model.PageResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static pl.lawit.domain.command.DocumentTemplateCommand.CreateTemplatedDocument;
import static pl.lawit.domain.command.DocumentTemplateCommand.FindTemplates;
import static pl.lawit.domain.command.DocumentTemplateCommand.GenerateTemplatedDocument;
import static pl.lawit.domain.command.PurchasedDocumentCommand.CreatePurchasedDocument;
import static pl.lawit.kernel.model.ApplicationMediaType.PDF;

@Service
@RequiredArgsConstructor
public class TemplateService {

	private final TemplateRepository templateRepository;

	private final PurchasedDocumentRepository purchasedDocumentRepository;

	private final RegisteredFileRepository registeredFileRepository;

	private final TemplateProcessor templateProcessor;

	private final FileService fileService;

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

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
	public FileDetail purchaseDocument(CreateTemplatedDocument command) {
		//Get existing template and file
		DocumentTemplate template = templateRepository.getByUuid(command.templateUuid());
		RegisteredFile registeredFile = registeredFileRepository.getByUuid(template.fileUuid());

		//Generate purchased document based on template file
		String fileName = generateFileName(template.templateName());
		GenerateTemplatedDocument command1 = GenerateTemplatedDocument.builder()
			.templateParameters(command.templateParameters())
			.templateName(fileName)
			.filePath(registeredFile.filePath())
			.build();

		FileDetail document = templateProcessor.generateDocument(command1);

		//Upload purchased document to file storage
		RegisteredFile purchasedDocumentFile = fileService.uploadFile(document, command.authenticatedUser());
		registerPurchasedDocument(command.authenticatedUser(), purchasedDocumentFile.uuid(), template.uuid());

		return document;
	}

	private String generateFileName(String value) {
		String formattedString = value.replaceAll("\\s+", "_");
		String currentDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);

		return formattedString + "_" + currentDate;
	}

	private PurchasedDocument registerPurchasedDocument(AuthenticatedUser authenticatedUser, UUID fileUuid,
														UUID templateUuid) {
		CreatePurchasedDocument command = CreatePurchasedDocument.builder()
			.authenticatedUser(authenticatedUser)
			.fileUuid(fileUuid)
			.templateUuid(templateUuid)
			.build();
		return purchasedDocumentRepository.create(command);
	}

	@Transactional
	public PageResult<PurchasedDocumentInfo> findUserTemplates(AuthenticatedUser authenticatedUser,
															   PageCommandQuery commandQuery) {
		PageResult<PurchasedDocument> page = purchasedDocumentRepository
			.findAllUserDocuments(authenticatedUser, commandQuery);
		List<PurchasedDocumentInfo> infoList = List.empty();

		for (PurchasedDocument document : page.content()) {
			RegisteredFile registeredFile = registeredFileRepository.getByUuid(document.fileUuid());
			DocumentTemplate documentTemplate = templateRepository.getByUuid(document.templateUuid());
			PurchasedDocumentInfo info = PurchasedDocumentInfo.builder()
				.purchasedDocument(document)
				.purchasedDocumentFile(registeredFile)
				.documentTemplate(documentTemplate)
				.build();
			infoList = infoList.append(info);
		}

		return PageResult.<PurchasedDocumentInfo>builder()
			.content(infoList)
			.pageIndex(page.pageIndex())
			.pageSize(page.pageSize())
			.totalPages(page.totalPages())
			.totalElements(page.totalElements())
			.build();
	}
}
