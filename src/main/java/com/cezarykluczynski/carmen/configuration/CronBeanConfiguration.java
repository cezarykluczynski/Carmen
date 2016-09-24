package com.cezarykluczynski.carmen.configuration;

import com.cezarykluczynski.carmen.cron.github.CarmenNoopTaskExecutor;
import com.cezarykluczynski.carmen.cron.github.CarmenSimpleAsyncTaskExecutor;
import com.cezarykluczynski.carmen.cron.management.service.DatabaseSwitchableJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class CronBeanConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @Profile("scheduled")
    @Primary
    public TaskExecutor carmenSimpleAsyncTaskExecutor() {
        return new CarmenSimpleAsyncTaskExecutor(applicationContext.getBean(DatabaseSwitchableJobsService.class),
                applicationContext.getBean(SimpleAsyncTaskExecutor.class));
    }

    @Bean
    @Profile("!scheduled")
    @Primary
    public TaskExecutor carmenNoopTaskExecutor() {
        return new CarmenNoopTaskExecutor();
    }

    @Bean(name = "simpleAsyncTaskExecutor")
    public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

}
