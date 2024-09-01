package pl.lawit.web.configuration;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;

public class VavrBaseConverter {

	static AnnotatedType getAnnotatedType(AnnotatedType annotatedType, JavaType javaType) {
		JavaType innerType = javaType.containedType(0);
		return new AnnotatedType()
			.type(innerType)
			.name(annotatedType.getName())
			.parent(annotatedType.getParent())
			.jsonUnwrappedHandler(annotatedType.getJsonUnwrappedHandler())
			.skipOverride(annotatedType.isSkipOverride())
			.schemaProperty(annotatedType.isSchemaProperty())
			.ctxAnnotations(annotatedType.getCtxAnnotations())
			.resolveAsRef(annotatedType.isResolveAsRef())
			.jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
			.skipSchemaName(annotatedType.isSkipSchemaName())
			.skipJsonIdentity(annotatedType.isSkipJsonIdentity())
			.propertyName(annotatedType.getPropertyName());
	}

}
