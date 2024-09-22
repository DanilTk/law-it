package pl.lawit.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lawit.domain.processor.UserSyncProcessor;

import static pl.lawit.kernel.logger.ApplicationLoggerFactory.cronLogger;

@Component
public class UserSynchronizationJob implements Job {

	@Autowired
	private UserSyncProcessor userSyncProcessor;

	@Override
	public void execute(JobExecutionContext context) {
		cronLogger().info("Starting user synchronization job.");
		userSyncProcessor.synchronizeUsers();
		cronLogger().info("User synchronization Job completed successfully.");
	}
}
