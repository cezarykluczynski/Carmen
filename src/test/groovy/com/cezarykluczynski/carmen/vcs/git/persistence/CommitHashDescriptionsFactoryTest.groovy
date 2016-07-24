package com.cezarykluczynski.carmen.vcs.git.persistence

import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter
import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash
import com.cezarykluczynski.carmen.vcs.git.model.CommitHashDescriptions
import com.cezarykluczynski.carmen.vcs.server.Server
import spock.lang.Specification

class CommitHashDescriptionsFactoryTest extends Specification {

    private static final String COMMIT_HASH = "abcdef1234abcdef1234abcdef1234abcdef1234"
    private static final String CLONE_ROOT = "a"
    private static final String LOCATION_DIRECTORY = "b"
    private static final String LOCATION_SUBDIRECTORY = "c"
    private static final String CLONE_DIRECTORY = CLONE_ROOT + "/" + LOCATION_DIRECTORY + "/" + LOCATION_SUBDIRECTORY

    private CommitHashDescriptionsFactory commitHashDescriptionsFactory

    private LangsStatsAdapter langsStatsAdapterMock

    private Server serverMock

    private RepositoryClone repositoryCloneMock

    private CommitHash commitHashMock

    private CommitDescription commitDescriptionMock

    private RepositoryDescription repositoryDescriptionMock

    def setup() {
        langsStatsAdapterMock = Mock LangsStatsAdapter
        serverMock = Mock Server
        repositoryCloneMock = Mock RepositoryClone
        commitHashMock = Mock CommitHash
        commitDescriptionMock = Mock CommitDescription
        repositoryDescriptionMock = Mock RepositoryDescription

        commitHashMock.getHash() >> COMMIT_HASH
        serverMock.getCloneRoot() >> CLONE_ROOT
        repositoryCloneMock.getLocationDirectory() >> LOCATION_DIRECTORY
        repositoryCloneMock.getLocationSubdirectory() >> LOCATION_SUBDIRECTORY
        langsStatsAdapterMock.describeCommit(CLONE_DIRECTORY, COMMIT_HASH) >> commitDescriptionMock
        langsStatsAdapterMock.describeRepository(CLONE_DIRECTORY, COMMIT_HASH) >> repositoryDescriptionMock

        commitHashDescriptionsFactory = new CommitHashDescriptionsFactory(langsStatsAdapterMock, serverMock)
    }

    void "creates using repository clone and commit hash"() {
        when:
        CommitHashDescriptions commitHashDescriptions = commitHashDescriptionsFactory
                .createUsingRepositoryCloneAndCommitHash(repositoryCloneMock, commitHashMock)

        then:
        commitHashDescriptions.getCommitHash().getHash() == COMMIT_HASH
        commitHashDescriptions.getCommitDescription() == commitDescriptionMock
        commitHashDescriptions.getRepositoryDescription() == repositoryDescriptionMock
    }

}
