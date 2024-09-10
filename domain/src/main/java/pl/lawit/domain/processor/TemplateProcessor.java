package pl.lawit.domain.processor;

import pl.lawit.kernel.model.FileDetail;

import static pl.lawit.domain.command.DocumentTemplateCommand.GenerateTemplatedDocument;

public interface TemplateProcessor {

	FileDetail generateDocument(GenerateTemplatedDocument command);

}
