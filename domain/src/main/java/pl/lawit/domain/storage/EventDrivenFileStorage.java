package pl.lawit.domain.storage;

import static pl.lawit.domain.event.TransactionalFileEvent.DeleteFileEvent;

public interface EventDrivenFileStorage extends FileStorage {

	void deleteFile(DeleteFileEvent event);
}
