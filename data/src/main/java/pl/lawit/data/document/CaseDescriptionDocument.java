package pl.lawit.data.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document(collation = "documents")
public class CaseDescriptionDocument {

	@Id
	private UUID uuid;

	private String description;

}
