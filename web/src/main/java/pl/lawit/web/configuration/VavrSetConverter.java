package pl.lawit.web.configuration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;

import static pl.lawit.web.configuration.VavrBaseConverter.getAnnotatedType;

@Component
@RequiredArgsConstructor
class VavrSetConverter implements ModelConverter {

	private final ObjectMapper objectMapper;

	@Override
	public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = objectMapper.constructType(annotatedType.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (Set.class.isAssignableFrom(cls)) {
				AnnotatedType elementType = getAnnotatedType(annotatedType, javaType);
				return new ArraySchema()
					.items(this.resolve(elementType, context, chain))
					.uniqueItems(true);
			}
		}
		if (chain.hasNext()) {
			return chain.next().resolve(annotatedType, context, chain);
		}
		return null;
	}

}
