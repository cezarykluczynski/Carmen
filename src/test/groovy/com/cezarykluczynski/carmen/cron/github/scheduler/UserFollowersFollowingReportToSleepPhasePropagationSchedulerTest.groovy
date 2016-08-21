package com.cezarykluczynski.carmen.cron.github.scheduler

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersFollowingReportToSleepPhasePropagationExecutor
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import spock.lang.Specification

class UserFollowersFollowingReportToSleepPhasePropagationSchedulerTest extends Specification {

    private UserFollowersFollowingReportToSleepPhasePropagationExecutor propagationExecutor

    private UserFollowersFollowingReportToSleepPhasePropagationScheduler propagationScheduler

    def setup() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        propagationExecutor = Mock UserFollowersFollowingReportToSleepPhasePropagationExecutor
        propagationScheduler = new UserFollowersFollowingReportToSleepPhasePropagationScheduler(taskExecutor,
                propagationExecutor)
    }

    def "executor runs"() {
        when:
        propagationScheduler.executePropagation()

        then:
        1 * propagationExecutor.run()
    }

}
