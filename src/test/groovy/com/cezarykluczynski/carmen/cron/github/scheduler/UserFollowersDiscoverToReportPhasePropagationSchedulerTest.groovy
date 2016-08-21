package com.cezarykluczynski.carmen.cron.github.scheduler

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersDiscoverToReportPhasePropagationExecutor
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import spock.lang.Specification

class UserFollowersDiscoverToReportPhasePropagationSchedulerTest extends Specification {

    private UserFollowersDiscoverToReportPhasePropagationExecutor propagationExecutor

    private UserFollowersDiscoverToReportPhasePropagationScheduler propagationScheduler

    def setup() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        propagationExecutor = Mock UserFollowersDiscoverToReportPhasePropagationExecutor
        propagationScheduler = new UserFollowersDiscoverToReportPhasePropagationScheduler(taskExecutor,
                propagationExecutor)
    }

    def "executor runs"() {
        when:
        propagationScheduler.executePropagation()

        then:
        1 * propagationExecutor.run()
    }

}
