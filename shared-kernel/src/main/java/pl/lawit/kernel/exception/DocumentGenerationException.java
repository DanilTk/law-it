package pl.lawit.kernel.exception;

public class DocumentGenerationException extends BaseException {

	public DocumentGenerationException(String templateName) {
		super("Error during document generation with name: " + templateName);
	}

}
