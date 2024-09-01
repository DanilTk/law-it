package pl.lawit.domain.event;

import lombok.NonNull;
import pl.lawit.kernel.model.FilePath;

public interface TransactionalFileEvent {

	record DeleteFileEvent(

		@NonNull
		FilePath filePath

	) {

		public static DeleteFileEvent of(@NonNull FilePath value) {
			return new DeleteFileEvent(value);
		}

	}
}
