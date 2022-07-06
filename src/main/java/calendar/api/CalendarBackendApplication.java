package calendar.api;

import org.jobrunr.configuration.JobRunr;
import org.jobrunr.configuration.JobRunrConfiguration;
import org.jobrunr.scheduling.JobRequestScheduler;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.sql.common.SqlStorageProviderFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@SpringBootApplication
public class CalendarBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarBackendApplication.class, args);
	}

	@Bean
	public JobRunrConfiguration.JobRunrConfigurationResult initJobRunr(ApplicationContext applicationContext) {
		return JobRunr.configure()
				.useJobActivator(applicationContext::getBean)
				.useStorageProvider(SqlStorageProviderFactory
						.using(applicationContext.getBean(DataSource.class)))
				.useBackgroundJobServer()
				.useJmxExtensions()
				.useDashboard()
				.initialize();
	}

	@Bean
	public JobScheduler initJobScheduler(JobRunrConfiguration.JobRunrConfigurationResult jobRunrConfigurationResult) {
		return jobRunrConfigurationResult.getJobScheduler();
	}

	@Bean
	public JobRequestScheduler initJobRequestScheduler(JobRunrConfiguration.JobRunrConfigurationResult jobRunrConfigurationResult) {
		return jobRunrConfigurationResult.getJobRequestScheduler();
	}

}
