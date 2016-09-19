package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.RepositoriesRepository
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest
import com.cezarykluczynski.carmen.util.DateTimeConstants
import spock.lang.Specification

class RepositoriesWakeUpExecutorTest extends Specification {

    private Integer refreshIntervalDays

    private RepositoriesRepository repositoriesRepository

    private PendingRequestFactory pendingRequestFactory

    private RepositoriesWakeUpExecutor repositoriesWakeUpExecutor

    private Repositories repositoriesEntity

    private PendingRequest pendingRequestEntity

    def setup() {
        refreshIntervalDays = 3
        repositoriesEntity = Mock Repositories
        pendingRequestEntity = Mock PendingRequest
        repositoriesRepository = Mock RepositoriesRepository
        pendingRequestFactory = Mock PendingRequestFactory
        pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(repositoriesEntity) >> pendingRequestEntity
        repositoriesWakeUpExecutor = new RepositoriesWakeUpExecutor(repositoriesRepository,
                pendingRequestFactory, refreshIntervalDays)
    }

    def "null entity does not throw exceptions"() {
        given:
        repositoriesRepository.findOldestPropagationInSleepPhase() >> null

        when:
        repositoriesWakeUpExecutor.run()

        then:
        noExceptionThrown()
    }

    def "not updatable entity is not updated"() {
        given:
        repositoriesRepository.findOldestPropagationInSleepPhase() >> repositoriesEntity

        when:
        repositoriesWakeUpExecutor.run()

        then:
        1 * repositoriesEntity.getUpdated() >> new Date()
        0 * pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(repositoriesEntity)
        0 * repositoriesRepository.moveToRefreshPhase(repositoriesEntity)
    }

    def "updatable entity is updated"() {
        given:
        repositoriesEntity.getUpdated() >> new Date(System.currentTimeMillis() - (- 1 + refreshIntervalDays * DateTimeConstants.MILLISECONDS_IN_DAY.getValue()))

        when:
        repositoriesWakeUpExecutor.run()

        then:
        1 * repositoriesRepository.findOldestPropagationInSleepPhase() >> repositoriesEntity
        1 * pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(repositoriesEntity)
        1 * repositoriesRepository.moveToRefreshPhase(repositoriesEntity)
    }

}
