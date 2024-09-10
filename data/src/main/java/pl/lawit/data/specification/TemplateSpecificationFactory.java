package pl.lawit.data.specification;

import io.vavr.collection.Set;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.lawit.data.entity.DocumentTemplateEntity;
import pl.lawit.data.entity.DocumentTemplateEntity_;
import pl.lawit.domain.model.Language;
import pl.lawit.domain.model.TemplateCategory;

import static pl.lawit.domain.command.DocumentTemplateCommand.FindTemplates;

@Component
public class TemplateSpecificationFactory extends BaseSpecificationFactory {

	public Specification<DocumentTemplateEntity> findCompanies(FindTemplates query) {
		return Specification.where(query.templateName().map(this::templateNameLike).getOrNull())
			.and(query.templateCategories().map(this::templateCategoryIn).getOrNull())
			.and(query.languages().map(this::languageCodeIn).getOrNull());
	}

	private Specification<DocumentTemplateEntity> templateNameLike(String name) {
		return whereLikeIgnoreCase(DocumentTemplateEntity_.templateName, name);
	}

	private Specification<DocumentTemplateEntity> templateCategoryIn(Set<TemplateCategory> collection) {
		return whereIn(DocumentTemplateEntity_.templateCategory, collection);
	}

	private Specification<DocumentTemplateEntity> languageCodeIn(Set<Language> collection) {
		return whereIn(DocumentTemplateEntity_.languageCode, collection);
	}

}
