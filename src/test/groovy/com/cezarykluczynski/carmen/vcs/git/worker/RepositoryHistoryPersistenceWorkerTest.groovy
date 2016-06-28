package com.cezarykluczynski.carmen.vcs.git.worker

import com.cezarykluczynski.carmen.vcs.git.persistence.GitHubRepositoryClonePersister
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

class RepositoryHistoryPersistenceWorkerTest {


    private GitHubRepositoryClonePersister gitHubRepositoryClonePersister

    private RepositoryHistoryPersistenceWorker repositoryHistoryPersistenceWorker

    @BeforeMethod
    void setup() {
        gitHubRepositoryClonePersister = mock GitHubRepositoryClonePersister
        doNothing().when(gitHubRepositoryClonePersister).persist()
        repositoryHistoryPersistenceWorker = new RepositoryHistoryPersistenceWorker(gitHubRepositoryClonePersister)
    }

    @Test
    void run() {
        // exercise
        repositoryHistoryPersistenceWorker.run()

        // assertion
        verify(gitHubRepositoryClonePersister).persist()
    }


}
