package com.cezarykluczynski.carmen.cron.github.scheduler

import com.cezarykluczynski.carmen.cron.github.executor.APIRequestExecutor
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import spock.lang.Specification

class APIRequestExecutorCallsAPIRequestSchedulerTest extends Specification {

    private APIRequestExecutor apiRequestExecutor

    private APIRequestScheduler apiRequestScheduler

    def setup() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        apiRequestExecutor = Mock APIRequestExecutor
        apiRequestScheduler = new APIRequestScheduler(taskExecutor, apiRequestExecutor)
    }

    def "executor runs"() {
        when:
        apiRequestScheduler.executePropagation()

        then:
        1 * apiRequestExecutor.run()
    }

}
