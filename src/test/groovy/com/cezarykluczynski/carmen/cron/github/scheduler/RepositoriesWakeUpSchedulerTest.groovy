package com.cezarykluczynski.carmen.cron.github.scheduler

import com.cezarykluczynski.carmen.cron.github.executor.RepositoriesWakeUpExecutor
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import spock.lang.Specification

class RepositoriesWakeUpSchedulerTest extends Specification {

    private RepositoriesWakeUpExecutor repositoriesWakeUpExecutor

    private RepositoriesWakeUpScheduler repositoriesWakeUpScheduler

    def setup() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        repositoriesWakeUpExecutor = Mock RepositoriesWakeUpExecutor
        repositoriesWakeUpScheduler = new RepositoriesWakeUpScheduler(taskExecutor, repositoriesWakeUpExecutor)
    }

    def "executor runs"() {
        when:
        repositoriesWakeUpScheduler.executePropagation()

        then:
        1 * repositoriesWakeUpExecutor.run()
    }

}
