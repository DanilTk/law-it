package pl.lawit.reporting;

import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import pl.lawit.domain.processor.TemplateProcessor;
import pl.lawit.domain.storage.FileStorage;
import pl.lawit.kernel.exception.DocumentGenerationException;
import pl.lawit.kernel.model.FileDetail;
import pl.lawit.kernel.model.FileName;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static pl.lawit.domain.command.DocumentTemplateCommand.GenerateTemplatedDocument;
import static pl.lawit.kernel.model.FileName.ILLEGAL_CHARACTERS;

@Component
@RequiredArgsConstructor
public class PdfProcesor implements TemplateProcessor {

	private static final String DOCUMENT_FORMAT = ".pdf";

	private final FileStorage fileStorage;

	@Override
	public FileDetail generateDocument(GenerateTemplatedDocument command) {
		String sanitizedName = ILLEGAL_CHARACTERS.matcher(command.templateName())
			.replaceAll("_").toLowerCase()
			.concat(DOCUMENT_FORMAT);

		try (InputStream inputStream = fileStorage.downloadFile(command.filePath());
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			PDDocument document = Loader.loadPDF(inputStream.readAllBytes());
			PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

			for (Tuple2<String, String> pair : command.templateParameters()) {
				populateTemplateParameter(acroForm, pair._1(), pair._2());
			}

			acroForm.flatten();

			document.save(outputStream);
			document.close();

			return FileDetail.builder()
				.fileName(FileName.of(sanitizedName))
				.fileContent(new ByteArrayResource(outputStream.toByteArray()))
				.build();
		} catch (IOException e) {
			throw new DocumentGenerationException(command.templateName());
		}
	}

	private void populateTemplateParameter(PDAcroForm acroForm, String fieldName, String value) throws IOException {
		PDField field = acroForm.getField(fieldName);
		field.setValue(value);
	}
}
