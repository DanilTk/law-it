package pl.lawit.domain.command;

import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.NonNull;
import pl.lawit.domain.model.Language;
import pl.lawit.domain.model.TemplateCategory;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.model.FilePath;

import java.util.UUID;

public interface DocumentTemplateCommand {

	@Builder(toBuilder = true)
	record CreateDocumentTemplate(

		@NonNull
		UUID fileUuid,

		@NonNull
		String fileName,

		@NonNull
		TemplateCategory templateCategory,

		@NonNull
		Language languageCode,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record CreateTemplatedDocument(

		@NotEmpty
		Map<String, String> templateParameters,

		@NonNull
		UUID templateUuid,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}

	@Builder(toBuilder = true)
	record GenerateTemplatedDocument(

		@NonNull
		FilePath filePath,

		@NotEmpty
		Map<String, String> templateParameters,

		@NonNull
		String templateName

	) {
	}

	@Builder(toBuilder = true)
	record FindTemplates(

		@NonNull
		Option<String> templateName,

		@NonNull
		Option<Set<TemplateCategory>> templateCategories,

		@NonNull
		Option<Set<Language>> languages,

		@NonNull
		AuthenticatedUser authenticatedUser

	) {
	}
}
