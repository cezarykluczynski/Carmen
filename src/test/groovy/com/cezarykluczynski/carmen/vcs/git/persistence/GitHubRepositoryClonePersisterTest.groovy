package com.cezarykluczynski.carmen.vcs.git.persistence

import com.beust.jcommander.internal.Lists
import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAO
import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.util.DateUtil
import com.cezarykluczynski.carmen.util.factory.DateFactory
import com.cezarykluczynski.carmen.util.factory.NowDateProvider
import com.cezarykluczynski.carmen.vcs.git.service.CommitHashPersistenceService
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Matchers.isA
import static org.mockito.Mockito.*

class GitHubRepositoryClonePersisterTest {

    private static final Date NOW = DateUtil.now()

    private GitHubRepositoryClonePersister gitHubRepositoryClonePersister

    private CommitHashPersistenceService commitHashPersistenceService

    private GitHubRepositoryCommitsToPersistFinder gitHubRepositoryCommitsToPersistFinder

    private RepositoriesClonesDAO repositoriesClonesDAO

    private DateFactory dateFactory

    private RepositoryClone repositoryClone

    @BeforeMethod
    void setup() {
        commitHashPersistenceService = mock CommitHashPersistenceService
        gitHubRepositoryCommitsToPersistFinder = mock GitHubRepositoryCommitsToPersistFinder
        repositoriesClonesDAO = mock RepositoriesClonesDAO
        dateFactory = new DateFactory(new NowDateProvider() {
            @Override
            public Date createNowDate() {
                return NOW
            }
        })

        repositoryClone = mock RepositoryClone

        gitHubRepositoryClonePersister = new GitHubRepositoryClonePersister(commitHashPersistenceService,
                gitHubRepositoryCommitsToPersistFinder, repositoriesClonesDAO, dateFactory)

    }

    @Test
    void "persist when there is no repository clones to process"() {
        // setup
        when repositoriesClonesDAO.findRepositoryCloneWithCommitsToPersist() thenReturn null
        when gitHubRepositoryCommitsToPersistFinder.getCommitHashesToPersist(null) thenReturn null

        // exercise
        gitHubRepositoryClonePersister.persist()

        // assertion
        verify gitHubRepositoryCommitsToPersistFinder, never() getCommitHashesToPersist null
    }

    @Test
    void "persist when there are error while retrieving hashes"() {
        // setup
        when repositoriesClonesDAO.findRepositoryCloneWithCommitsToPersist() thenReturn repositoryClone
        when gitHubRepositoryCommitsToPersistFinder.getCommitHashesToPersist(repositoryClone) thenReturn null
        when repositoriesClonesDAO.update(isA(RepositoryClone.class)) thenReturn null
        when repositoriesClonesDAO.update(null) thenReturn null

        // exercise
        gitHubRepositoryClonePersister.persist()

        // assertion
        verify(repositoriesClonesDAO, never()).update isA(RepositoryClone.class)
        verify(repositoriesClonesDAO, never()).update null
    }


    @Test
    void "sets commitsStatisticsUntil field to the beginning o not yet opened month, when commit list is empty"() {
        // setup
        doNothing().when(repositoryClone).setCommitsStatisticsUntil isA(Date.class)
        when(repositoriesClonesDAO.update(repositoryClone)).thenReturn null
        when repositoriesClonesDAO.findRepositoryCloneWithCommitsToPersist() thenReturn repositoryClone
        when gitHubRepositoryCommitsToPersistFinder.getCommitHashesToPersist(repositoryClone) thenReturn Lists.newArrayList()

        // exercise
        gitHubRepositoryClonePersister.persist()

        // assertion
        verify repositoryClone setCommitsStatisticsUntil(isA(Date.class))
        verify repositoriesClonesDAO update repositoryClone
    }

}
