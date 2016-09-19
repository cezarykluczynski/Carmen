package com.cezarykluczynski.carmen.vcs.git.service

import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription
import com.cezarykluczynski.carmen.lang.stats.persistence.Persister
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.RepositoryClone
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash
import com.cezarykluczynski.carmen.vcs.git.model.CommitHashDescriptions
import com.cezarykluczynski.carmen.vcs.git.persistence.CommitHashDescriptionsFactory
import spock.lang.Specification

public class CommitHashPersistenceServiceTest extends Specification {

    private CommitHashDescriptionsFactory commitHashDescriptionsFactoryMock

    private Persister persisterMock

    private CommitHash commitHashMock

    private RepositoryClone repositoryCloneMock

    private CommitHashDescriptions commitHashDescriptionsMock

    private CommitDescription commitDescriptionMock

    private RepositoryDescription repositoryDescriptionMock

    private CommitHashPersistenceService commitHashPersistenceService

    def setup() {
        commitHashDescriptionsFactoryMock = Mock CommitHashDescriptionsFactory
        persisterMock = Mock Persister
        commitHashMock = Mock CommitHash
        repositoryCloneMock = Mock RepositoryClone
        commitDescriptionMock = Mock CommitDescription
        repositoryDescriptionMock = Mock RepositoryDescription
        commitHashDescriptionsMock = Mock CommitHashDescriptions
        commitHashDescriptionsMock.getCommitDescription() >> commitDescriptionMock
        commitHashDescriptionsMock.getRepositoryDescription() >> repositoryDescriptionMock
        commitHashPersistenceService = new CommitHashPersistenceService(commitHashDescriptionsFactoryMock, persisterMock)
    }

    def persistAndDescribeCommitHashUsingRepositoryClone() {
        given:
        commitHashDescriptionsFactoryMock.createUsingRepositoryCloneAndCommitHash(repositoryCloneMock,
                commitHashMock) >> commitHashDescriptionsMock

        when:
        CommitHashDescriptions commitHashDescriptionsResponse = commitHashPersistenceService
                .persistAndDescribeCommitHashUsingRepositoryClone commitHashMock, repositoryCloneMock

        then:
        commitHashDescriptionsResponse == commitHashDescriptionsMock
        1 * persisterMock.persist(commitDescriptionMock)
        1 * persisterMock.persist(repositoryDescriptionMock)
    }

}
