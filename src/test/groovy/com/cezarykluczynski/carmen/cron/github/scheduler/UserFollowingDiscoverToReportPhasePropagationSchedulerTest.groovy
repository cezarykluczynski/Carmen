package com.cezarykluczynski.carmen.cron.github.scheduler

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowingDiscoverToReportPhasePropagationExecutor
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import spock.lang.Specification

class UserFollowingDiscoverToReportPhasePropagationSchedulerTest extends Specification {

    private UserFollowingDiscoverToReportPhasePropagationExecutor propagationExecutor

    private UserFollowingDiscoverToReportPhasePropagationScheduler propagationScheduler

    def setup() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        propagationExecutor = Mock UserFollowingDiscoverToReportPhasePropagationExecutor
        propagationScheduler = new UserFollowingDiscoverToReportPhasePropagationScheduler(taskExecutor,
                propagationExecutor)
    }

    def "executor runs"() {
        when:
        propagationScheduler.executePropagation()

        then:
        1 * propagationExecutor.run()
    }

}
