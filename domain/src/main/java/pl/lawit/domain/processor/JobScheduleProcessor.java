package pl.lawit.domain.processor;

import java.time.Instant;
import java.util.UUID;

public interface JobScheduleProcessor {

	void scheduleLegalCaseWithdrawal(UUID uuid, Instant scheduledTime);

	void scheduleLegalCaseCompletion(UUID uuid, Instant scheduledTime);

	void unscheduleLegalCaseJobs(UUID uuid);

}
