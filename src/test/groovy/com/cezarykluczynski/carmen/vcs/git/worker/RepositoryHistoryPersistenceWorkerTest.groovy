package com.cezarykluczynski.carmen.vcs.git.worker

import com.cezarykluczynski.carmen.vcs.git.persistence.vendor.github.GitHubRepositoryClonePersister
import spock.lang.Specification

class RepositoryHistoryPersistenceWorkerTest extends Specification {

    private GitHubRepositoryClonePersister gitHubRepositoryClonePersisterMock

    private RepositoryHistoryPersistenceWorker repositoryHistoryPersistenceWorker

    def setup() {
        gitHubRepositoryClonePersisterMock = Mock GitHubRepositoryClonePersister
        repositoryHistoryPersistenceWorker = new RepositoryHistoryPersistenceWorker(gitHubRepositoryClonePersisterMock)
    }

    def "calls persister's perist when being run"() {
        when:
        repositoryHistoryPersistenceWorker.run()

        then:
        1 * gitHubRepositoryClonePersisterMock.persist()
    }


}
