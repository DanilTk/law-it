package pl.lawit.web.configuration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
@RequiredArgsConstructor
class VavrSeqConverter implements ModelConverter {

	private final ObjectMapper objectMapper;

	@Override
	public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = objectMapper.constructType(annotatedType.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (Seq.class.isAssignableFrom(cls)) {
				JavaType innerType = javaType.containedType(0);
				ArrayType targetType = ArrayType.construct(innerType, null);
				AnnotatedType type = new AnnotatedType()
					.type(targetType)
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
