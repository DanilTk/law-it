package pl.lawit.kernel.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.UUID;

import static pl.lawit.kernel.exception.ObjectFieldName.EMAIL;
import static pl.lawit.kernel.exception.ObjectFieldName.UID;

@Slf4j
@Getter
public class ObjectNotFoundException extends BaseException {

	@NonNull
	private final String friendlyMessage;

	private ObjectNotFoundException(@NonNull String friendlyMessage) {
		super(friendlyMessage);
		this.friendlyMessage = friendlyMessage;
	}

	public ObjectNotFoundException() {
		this("Object not found.");
	}

	public static ObjectNotFoundException byUUID(@Nullable UUID uuid) {
		return create(String.valueOf(uuid), ObjectFieldName.UUID, null);
	}

	public static ObjectNotFoundException byUUID(@Nullable UUID uuid, @NonNull Class<?> objectClass) {
		return create(String.valueOf(uuid), ObjectFieldName.UUID, objectClass.getSimpleName());
	}

	public static ObjectNotFoundException byUid(@Nullable String uid, @NonNull Class<?> objectClass) {
		return create(uid, UID, objectClass.getSimpleName());
	}

	public static ObjectNotFoundException byFieldName(@Nullable String fieldValue,
													  @NonNull ObjectFieldName fieldName,
													  @NonNull Class<?> objectClass) {
		return create(fieldValue, fieldName, objectClass.getSimpleName());
	}

	public static ObjectNotFoundException byEmail(String value, @NonNull Class<?> objectClass) {
		return create(value, EMAIL, objectClass.getSimpleName());
	}

	private static ObjectNotFoundException create(@Nullable String fieldValue, @NonNull ObjectFieldName fieldName,
												  @Nullable String objectType) {
		String convertedFieldName = convertCamelCaseToWords(objectType);
		String formatted = String.format("%s searched by %s: %s not found",
			objectType != null ? convertedFieldName : "Object", fieldName.toString().toLowerCase(), fieldValue);

		ObjectNotFoundException exception = new ObjectNotFoundException(formatted);
		log.error(exception.toString());
		return exception;
	}

	private static String convertCamelCaseToWords(String camelCase) {
		if (camelCase == null || camelCase.isEmpty()) {
			return camelCase;
		}

		String[] words = StringUtils.splitByCharacterTypeCamelCase(camelCase);

		for (int i = 0; i < words.length; i++) {
			words[i] = StringUtils.capitalize(words[i]);
		}

		return String.join(" ", words);
	}
}
