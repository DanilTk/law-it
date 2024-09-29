package pl.lawit.scheduler;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.processor.JobScheduleProcessor;
import pl.lawit.scheduler.job.LegalCaseCompletionJob;
import pl.lawit.scheduler.job.LegalCaseExpirationJob;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static pl.lawit.scheduler.ScheduleGroup.COMPLETE_LEGAL_CASES;
import static pl.lawit.scheduler.ScheduleGroup.EXPIRED_LEGAL_CASES;

@Component
@RequiredArgsConstructor
public class DefaultJobScheduleProcessor implements JobScheduleProcessor {

	private final QuartzJobScheduler quartzJobScheduler;

	private static final String KEY_SEPARATOR = "_";

	@Override
	public void scheduleLegalCaseWithdrawal(UUID uuid, Instant scheduledTime) {
		Date scheduledDateTime = Date.from(scheduledTime);

		ScheduleProperties scheduleProperties = ScheduleProperties.builder()
			.key(uuid.toString())
			.scheduleGroup(EXPIRED_LEGAL_CASES)
			.jobClass(LegalCaseExpirationJob.class)
			.scheduleTime(scheduledDateTime)
			.description(Option.none())
			.build();

		quartzJobScheduler.scheduleJob(scheduleProperties);
	}

	@Override
	public void scheduleLegalCaseCompletion(UUID uuid, Instant scheduledTime) {
		Date scheduledDateTime = Date.from(scheduledTime);

		ScheduleProperties scheduleProperties = ScheduleProperties.builder()
			.key(uuid.toString())
			.scheduleGroup(COMPLETE_LEGAL_CASES)
			.jobClass(LegalCaseCompletionJob.class)
			.scheduleTime(scheduledDateTime)
			.description(Option.none())
			.build();

		quartzJobScheduler.scheduleJob(scheduleProperties);
	}

	@Override
	public void unscheduleLegalCaseJobs(UUID uuid) {
		quartzJobScheduler.unscheduleJob(uuid.toString(), EXPIRED_LEGAL_CASES);
		quartzJobScheduler.unscheduleJob(uuid.toString(), COMPLETE_LEGAL_CASES);
	}

	private void scheduleOrUpdateJob(ScheduleProperties scheduleProperties) {
		if (quartzJobScheduler.isTriggerExists(scheduleProperties.key(), scheduleProperties.scheduleGroup())) {
			quartzJobScheduler.rescheduleJob(scheduleProperties);
		} else {
			quartzJobScheduler.scheduleJob(scheduleProperties);
		}

	}

}
