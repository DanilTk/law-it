package pl.lawit.scheduler.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class SchedulerConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(QuartzProperties quartzProperties,
													 DataSource dataSource,
													 ApplicationContext applicationContext) {

		SchedulerJobFactory schedulerJobFactory = new SchedulerJobFactory();
		schedulerJobFactory.setApplicationContext(applicationContext);

		Properties properties = new Properties();
		properties.putAll(quartzProperties.getProperties());

		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setQuartzProperties(properties);
		factory.setJobFactory(schedulerJobFactory);

		return factory;
	}

	@Bean
	@Primary
	public Scheduler applicationScheduler(SchedulerFactoryBean factory) throws SchedulerException {
		Scheduler scheduler = factory.getScheduler();
		scheduler.start();
		return scheduler;
	}

}
