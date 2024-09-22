package pl.lawit.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.SchedulingException;
import org.springframework.stereotype.Component;
import pl.lawit.kernel.util.UuidProvider;

import static pl.lawit.kernel.logger.ApplicationLoggerFactory.cronLogger;

@Component
public class QuartzJobScheduler {

	private final Scheduler scheduler;

	private final UuidProvider uuidProvider;

	public QuartzJobScheduler(@Qualifier("applicationScheduler") Scheduler scheduler, UuidProvider uuidProvider) {
		this.scheduler = scheduler;
		this.uuidProvider = uuidProvider;
	}

	public void scheduleJob(ScheduleProperties scheduleProperties) {
		JobKey jobKey = new JobKey(uuidProvider.getUuid().toString(), scheduleProperties.scheduleGroup().name());

		JobDetail job = JobBuilder.newJob(scheduleProperties.jobClass())
			.withIdentity(jobKey)
			.build();

		SimpleTrigger trigger = buildSimpleTrigger(scheduleProperties);

		try {
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			cronLogger().error("Error scheduling job", e);
			throw new SchedulingException("Error scheduling job", e);
		}

		cronLogger().info("Job: {} scheduled to: {}.", trigger.getKey(), scheduleProperties.scheduleTime());
	}

	public void rescheduleJob(ScheduleProperties scheduleProperties) {
		TriggerKey triggerKey = new TriggerKey(scheduleProperties.key(), scheduleProperties.scheduleGroup().name());

		SimpleTrigger newTrigger = buildSimpleTrigger(scheduleProperties);

		try {
			scheduler.rescheduleJob(triggerKey, newTrigger);
		} catch (SchedulerException e) {
			cronLogger().error("Error rescheduling job", e);
			throw new SchedulingException("Error rescheduling job", e);
		}

		cronLogger().info("Job: {} rescheduled to: {}.", triggerKey, scheduleProperties.scheduleTime());
	}

	public void unscheduleJob(String key, ScheduleGroup scheduleGroup) {
		JobKey jobKey;
		TriggerKey triggerKey = new TriggerKey(key, scheduleGroup.name());

		try {
			Trigger trigger = scheduler.getTrigger(triggerKey);
			jobKey = trigger.getJobKey();
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			cronLogger().error("Error unscheduling job", e);
			throw new SchedulingException("Error unscheduling job", e);
		}

		cronLogger().info("Job: {} unscheduled.", jobKey);
	}

	public boolean isTriggerExists(String key, ScheduleGroup scheduleGroup) {
		TriggerKey triggerKey = new TriggerKey(key, scheduleGroup.name());

		try {
			return scheduler.checkExists(triggerKey);
		} catch (SchedulerException e) {
			cronLogger().error("Error checking if trigger exists", e);
			throw new SchedulingException("Error checking if trigger exists", e);
		}
	}

	private static SimpleTrigger buildSimpleTrigger(ScheduleProperties scheduleProperties) {
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
			.withMisfireHandlingInstructionFireNow();

		TriggerKey triggerKey = new TriggerKey(scheduleProperties.key(), scheduleProperties.scheduleGroup().name());

		SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
			.startAt(scheduleProperties.scheduleTime())
			.withIdentity(triggerKey)
			.withSchedule(scheduleBuilder)
			.build();

		if (scheduleProperties.description().isDefined()) {
			simpleTrigger = simpleTrigger.getTriggerBuilder()
				.withDescription(scheduleProperties.description().get())
				.build();
		}

		return simpleTrigger;
	}

}
