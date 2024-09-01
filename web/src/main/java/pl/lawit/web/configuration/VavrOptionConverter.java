package pl.lawit.web.configuration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
@RequiredArgsConstructor
class VavrOptionConverter implements ModelConverter {

	private final ObjectMapper objectMapper;

	@Override
	public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = objectMapper.constructType(annotatedType.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (Option.class.isAssignableFrom(cls)) {
				JavaType innerType = javaType.containedType(0);
				AnnotatedType type = new AnnotatedType()
					.type(innerType)
					.ctxAnnotations(annotatedType.getCtxAnnotations())
					.parent(annotatedType.getParent())
					.schemaProperty(annotatedType.isSchemaProperty())
					.name(annotatedType.getName())
					.resolveAsRef(annotatedType.isResolveAsRef())
					.jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
					.propertyName(annotatedType.getPropertyName())
					.skipOverride(true);
				return this.resolve(type, context, chain);
			}
		}
		if (chain.hasNext()) {
			return chain.next().resolve(annotatedType, context, chain);
		}
		return null;
	}

}
