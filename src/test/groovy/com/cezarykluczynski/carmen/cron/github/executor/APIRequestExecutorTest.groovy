package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository
import com.cezarykluczynski.carmen.executor.github.RepositoriesExecutor
import com.cezarykluczynski.carmen.executor.github.UserGhostExecutor
import com.cezarykluczynski.carmen.executor.github.UserGhostPaginatorExecutor
import spock.lang.Specification

class APIRequestExecutorTest extends Specification {

    private static final String REPOSITORIES = "Repositories"
    private static final String USERS_GHOST_PAGINATOR = "UsersGhostPaginator"
    private static final String USER_GHOST = "UserGhost"

    private PendingRequestRepository pendingRequestRepository

    private RepositoriesExecutor repositoriesExecutorMock

    private UserGhostPaginatorExecutor userGhostPaginatorExecutorMock

    private UserGhostExecutor userGhostExecutorMock

    private APIRequestExecutor apiRequestExecutor

    private PendingRequest pendingRequestEntity

    def setup() {
        pendingRequestRepository = Mock PendingRequestRepository
        repositoriesExecutorMock = Mock RepositoriesExecutor
        userGhostPaginatorExecutorMock = Mock UserGhostPaginatorExecutor
        userGhostExecutorMock = Mock UserGhostExecutor
        apiRequestExecutor = new APIRequestExecutor(pendingRequestRepository, repositoriesExecutorMock,
                userGhostPaginatorExecutorMock, userGhostExecutorMock)
    }

    def "should not run when there is no pending request"() {
        when:
        apiRequestExecutor.run()

        then:
        1 * pendingRequestRepository.findMostImportantPendingRequest() >> null
        0 * repositoriesExecutorMock.execute(*_)
        0 * userGhostPaginatorExecutorMock.execute(*_)
        0 * userGhostExecutorMock.execute(*_)
    }

    def "should run Repositories"() {
        given:
        pendingRequestEntity = new PendingRequest(executor: REPOSITORIES)

        when:
        apiRequestExecutor.run()

        then:
        1 * pendingRequestRepository.findMostImportantPendingRequest() >> pendingRequestEntity
        1 * repositoriesExecutorMock.execute(pendingRequestEntity)
        0 * userGhostPaginatorExecutorMock.execute(*_)
        0 * userGhostExecutorMock.execute(*_)
    }

    def "should run Repositories and tolerate exception"() {
        given:
        pendingRequestEntity = new PendingRequest(executor: REPOSITORIES)

        when:
        apiRequestExecutor.run()

        then:
        1 * pendingRequestRepository.findMostImportantPendingRequest() >> pendingRequestEntity
        1 * repositoriesExecutorMock.execute(pendingRequestEntity) >> { args ->
            throw new IOException()
        }
        noExceptionThrown()
    }

    def "should run UsersGhostPaginator"() {
        given:
        pendingRequestEntity = new PendingRequest(executor: USERS_GHOST_PAGINATOR)

        when:
        apiRequestExecutor.run()

        then:
        1 * pendingRequestRepository.findMostImportantPendingRequest() >> pendingRequestEntity
        0 * repositoriesExecutorMock.execute(*_)
        1 * userGhostPaginatorExecutorMock.execute(pendingRequestEntity)
        0 * userGhostExecutorMock.execute(*_)
    }

    def "should run UsersGhostPaginator and tolerate exception"() {
        given:
        pendingRequestEntity = new PendingRequest(executor: USERS_GHOST_PAGINATOR)

        when:
        apiRequestExecutor.run()

        then:
        1 * pendingRequestRepository.findMostImportantPendingRequest() >> pendingRequestEntity
        1 * userGhostPaginatorExecutorMock.execute(pendingRequestEntity) >> { args ->
            throw new IOException()
        }
        noExceptionThrown()
    }

    def "should run UserGhost"() {
        given:
        pendingRequestEntity = new PendingRequest(executor: USER_GHOST)

        when:
        apiRequestExecutor.run()

        then:
        1 * pendingRequestRepository.findMostImportantPendingRequest() >> pendingRequestEntity
        0 * repositoriesExecutorMock.execute(*_)
        0 * userGhostPaginatorExecutorMock.execute(*_)
        1 * userGhostExecutorMock.execute(pendingRequestEntity)
    }

    def "should run UserGhost and tolerate exception"() {
        given:
        pendingRequestEntity = new PendingRequest(executor: USER_GHOST)

        when:
        apiRequestExecutor.run()

        then:
        1 * pendingRequestRepository.findMostImportantPendingRequest() >> pendingRequestEntity
        1 * userGhostExecutorMock.execute(pendingRequestEntity) >> { args ->
            throw new IOException()
        }
        noExceptionThrown()
    }

}
