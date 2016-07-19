package com.cezarykluczynski.carmen.cron.github

import com.cezarykluczynski.carmen.cron.management.service.DatabaseSwitchableJobsService
import org.springframework.core.task.SimpleAsyncTaskExecutor
import spock.lang.Specification

class CarmenSimpleAsyncTaskExecutorTest extends Specification {

    private static final String NO_TASKS = 'noScheduledTasks'

    private static String noTasks = null

    private CarmenSimpleAsyncTaskExecutor executor

    private DatabaseSwitchableJobsService databaseSwitchableJobsServiceMock

    private SimpleAsyncTaskExecutor simpleAsyncTaskExecutorMock

    private Runnable runnable

    def setup() {
        databaseSwitchableJobsServiceMock = Mock DatabaseSwitchableJobsService
        simpleAsyncTaskExecutorMock = Mock SimpleAsyncTaskExecutor
        runnable = Mock Runnable
        executor = new CarmenSimpleAsyncTaskExecutor(databaseSwitchableJobsServiceMock, simpleAsyncTaskExecutorMock)
        noTasks = System.getProperty NO_TASKS

    }

    def cleanup() {
        if (noTasks != null) {
            System.setProperty NO_TASKS, noTasks
        }
    }

    def "does not run when noTasks is set"() {
        given:
        System.setProperty NO_TASKS, "true"

        when:
        executor.execute runnable

        then:
        0 * simpleAsyncTaskExecutorMock.execute(runnable)
    }

    def "should not run when task is disabled"() {
        given:
        System.clearProperty NO_TASKS
        databaseSwitchableJobsServiceMock.isEnabledOrNotDatabaseSwitchable(_) >> false

        when:
        executor.execute runnable

        then:
        0 * simpleAsyncTaskExecutorMock.execute(runnable)
    }

    def "should run"() {
        given:
        System.clearProperty NO_TASKS
        databaseSwitchableJobsServiceMock.isEnabledOrNotDatabaseSwitchable(_) >> true

        when:
        executor.execute runnable

        then:
        1 * simpleAsyncTaskExecutorMock.execute(runnable)
    }

}
