package pl.lawit.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class LegalCaseCompletionJob implements Job {

	@Override
	public void execute(JobExecutionContext context) {
		//to be implemented
	}
}
