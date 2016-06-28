package com.cezarykluczynski.carmen.vcs.git.service

import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription
import com.cezarykluczynski.carmen.lang.stats.persistence.Persister
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash
import com.cezarykluczynski.carmen.vcs.git.model.CommitHashDescriptions
import com.cezarykluczynski.carmen.vcs.git.persistence.CommitHashDescriptionsFactory
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

public class CommitHashPersistenceServiceTest {

    private CommitHashPersistenceService commitHashPersistenceService

    private CommitHashDescriptionsFactory commitHashDescriptionsFactory

    private Persister persister

    private CommitHash commitHash

    private RepositoryClone repositoryClone

    private CommitHashDescriptions commitHashDescriptions

    private CommitDescription commitDescription

    private RepositoryDescription repositoryDescription

    @BeforeMethod
    void setup() {
        commitHashDescriptionsFactory = mock CommitHashDescriptionsFactory
        persister = mock Persister
        commitHash = mock CommitHash
        repositoryClone = mock RepositoryClone
        commitDescription = mock CommitDescription
        repositoryDescription = mock RepositoryDescription
        commitHashDescriptions = mock CommitHashDescriptions
        when commitHashDescriptions.getCommitDescription() thenReturn commitDescription
        when commitHashDescriptions.getRepositoryDescription() thenReturn repositoryDescription
        commitHashPersistenceService = new CommitHashPersistenceService(commitHashDescriptionsFactory, persister)
    }

    @Test
    void persistAndDescribeCommitHashUsingRepositoryClone() {
        // setup
        when(commitHashDescriptionsFactory.createUsingRepositoryCloneAndCommitHash(repositoryClone, commitHash))
                .thenReturn commitHashDescriptions
        doNothing().when(persister).persist commitDescription
        doNothing().when(persister).persist repositoryDescription

        // exercise
        CommitHashDescriptions commitHashDescriptionsResponse = commitHashPersistenceService
                .persistAndDescribeCommitHashUsingRepositoryClone commitHash, repositoryClone

        // assertion
        println commitHashDescriptionsResponse
        Assert.assertEquals commitHashDescriptionsResponse, commitHashDescriptions
        verify(persister).persist commitHashDescriptionsResponse.getCommitDescription()
        verify(persister).persist commitHashDescriptionsResponse.getRepositoryDescription()
    }

}
