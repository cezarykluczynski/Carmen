package com.cezarykluczynski.carmen.configuration;

import com.cezarykluczynski.carmen.cron.github.CarmenNoopTaskExecutor;
import com.cezarykluczynski.carmen.cron.github.CarmenSimpleAsyncTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class CronBeanConfiguration {

    @Bean
    @Profile("scheduled")
    public TaskExecutor carmenSimpleAsyncTaskExecutor() {
        return new CarmenSimpleAsyncTaskExecutor();
    }

    @Bean
    @Profile("!scheduled")
    public TaskExecutor carmenNoopTaskExecutor() {
        return new CarmenNoopTaskExecutor();
    }

}
