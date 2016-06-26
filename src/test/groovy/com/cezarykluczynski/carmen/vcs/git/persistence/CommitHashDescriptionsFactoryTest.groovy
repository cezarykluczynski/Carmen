package com.cezarykluczynski.carmen.vcs.git.persistence

import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter
import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash
import com.cezarykluczynski.carmen.vcs.git.model.CommitHashDescriptions
import com.cezarykluczynski.carmen.vcs.server.Server
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class CommitHashDescriptionsFactoryTest {

    private static final String COMMIT_HASH = "abcdef1234abcdef1234abcdef1234abcdef1234"
    private static final String CLONE_ROOT = "a"
    private static final String LOCATION_DIRECTORY = "b"
    private static final String LOCATION_SUBDIRECTORY = "c"
    private static final String CLONE_DIRECTORY = CLONE_ROOT + "/" + LOCATION_DIRECTORY + "/" + LOCATION_SUBDIRECTORY


    private CommitHashDescriptionsFactory commitHashDescriptionsFactory

    private LangsStatsAdapter langsStatsAdapter

    private Server server

    private RepositoryClone repositoryClone

    private CommitHash commitHash

    private CommitDescription commitDescription

    private RepositoryDescription repositoryDescription

    @BeforeMethod
    void setup() {
        langsStatsAdapter = mock LangsStatsAdapter
        server = mock Server
        repositoryClone = mock RepositoryClone
        commitHash = mock CommitHash
        commitDescription = mock CommitDescription
        repositoryDescription = mock RepositoryDescription

        when commitHash.getHash() thenReturn COMMIT_HASH
        when server.getCloneRoot() thenReturn CLONE_ROOT
        when repositoryClone.getLocationDirectory() thenReturn LOCATION_DIRECTORY
        when repositoryClone.getLocationSubdirectory() thenReturn LOCATION_SUBDIRECTORY
        when langsStatsAdapter.describeCommit(CLONE_DIRECTORY, COMMIT_HASH) thenReturn commitDescription
        when langsStatsAdapter.describeRepository(CLONE_DIRECTORY, COMMIT_HASH) thenReturn repositoryDescription

        commitHashDescriptionsFactory = new CommitHashDescriptionsFactory(langsStatsAdapter, server)
    }


    @Test
    void createUsingRepositoryCloneAndCommitHash() {
        // exercise
        CommitHashDescriptions commitHashDescriptions = commitHashDescriptionsFactory
                .createUsingRepositoryCloneAndCommitHash(repositoryClone, commitHash)

        // assertion
        Assert.assertEquals commitHashDescriptions.getCommitHash().getHash(), COMMIT_HASH
        Assert.assertEquals commitHashDescriptions.getCommitDescription(), commitDescription
        Assert.assertEquals commitHashDescriptions.getRepositoryDescription(), repositoryDescription
    }

}
