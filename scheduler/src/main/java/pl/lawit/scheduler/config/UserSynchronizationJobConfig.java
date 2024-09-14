package pl.lawit.scheduler.config;

import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import pl.lawit.scheduler.job.UserSynchronizationJob;

@Configuration
@RequiredArgsConstructor
public class UserSynchronizationJobConfig {

	@Value("${application.cron.user-synchronzation}")
	private String cronExpression;

	@Bean
	public JobDetailFactoryBean userSynchronizationJobDetail() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(UserSynchronizationJob.class);
		jobDetailFactory.setDescription("Synchornize users");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}

	@Bean
	public CronTriggerFactoryBean userSynchronizationJobTrigger(@Qualifier("userSynchronizationJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
		triggerFactoryBean.setJobDetail(jobDetail);
		triggerFactoryBean.setCronExpression(cronExpression);
		return triggerFactoryBean;
	}

}
