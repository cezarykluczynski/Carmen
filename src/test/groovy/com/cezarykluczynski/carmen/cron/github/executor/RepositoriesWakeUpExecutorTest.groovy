package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAO
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.propagations.Repositories
import com.cezarykluczynski.carmen.util.DateTimeConstants
import spock.lang.Specification

class RepositoriesWakeUpExecutorTest extends Specification {

    private Integer refreshIntervalDays

    private RepositoriesDAO propagationsRepositoriesDAOImpl

    private PendingRequestFactory pendingRequestFactory

    private RepositoriesWakeUpExecutor repositoriesWakeUpExecutor

    private Repositories repositoriesEntity

    private PendingRequest pendingRequestEntity

    def setup() {
        refreshIntervalDays = 3
        repositoriesEntity = Mock Repositories
        pendingRequestEntity = Mock PendingRequest
        propagationsRepositoriesDAOImpl = Mock RepositoriesDAO
        pendingRequestFactory = Mock PendingRequestFactory
        pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(repositoriesEntity) >> pendingRequestEntity
        repositoriesWakeUpExecutor = new RepositoriesWakeUpExecutor(propagationsRepositoriesDAOImpl,
                pendingRequestFactory, refreshIntervalDays)
    }

    def "null entity does not throw exceptions"() {
        given:
        propagationsRepositoriesDAOImpl.findOldestPropagationInSleepPhase() >> null

        when:
        repositoriesWakeUpExecutor.run()

        then:
        noExceptionThrown()
    }

    def "not updatable entity is not updated"() {
        given:
        propagationsRepositoriesDAOImpl.findOldestPropagationInSleepPhase() >> repositoriesEntity

        when:
        repositoriesWakeUpExecutor.run()

        then:
        1 * repositoriesEntity.getUpdated() >> new Date()
        0 * pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(repositoriesEntity)
        0 * propagationsRepositoriesDAOImpl.moveToRefreshPhase(repositoriesEntity)
    }

    def "updatable entity is updated"() {
        given:
        repositoriesEntity.getUpdated() >> new Date(System.currentTimeMillis() - (- 1 + refreshIntervalDays * DateTimeConstants.MILLISECONDS_IN_DAY.getValue()))

        when:
        repositoriesWakeUpExecutor.run()

        then:
        1 * propagationsRepositoriesDAOImpl.findOldestPropagationInSleepPhase() >> repositoriesEntity
        1 * pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(repositoriesEntity)
        1 * propagationsRepositoriesDAOImpl.moveToRefreshPhase(repositoriesEntity)
    }

}
