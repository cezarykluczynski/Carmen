package com.cezarykluczynski.carmen.vcs.git.persistence

import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAO
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.util.DateUtil
import com.cezarykluczynski.carmen.util.factory.DateFactory
import com.cezarykluczynski.carmen.util.factory.NowDateProvider
import com.cezarykluczynski.carmen.vcs.git.service.CommitHashPersistenceService
import com.google.common.collect.Lists
import spock.lang.Specification

class GitHubRepositoryClonePersisterTest extends Specification {

    private static final Date NOW = DateUtil.now()

    private GitHubRepositoryClonePersister gitHubRepositoryClonePersister

    private CommitHashPersistenceService commitHashPersistenceServiceMock

    private GitHubRepositoryCommitsToPersistFinder gitHubRepositoryCommitsToPersistFinderMock

    private RepositoriesClonesDAO repositoriesClonesDAOMock

    private DateFactory dateFactoryMock

    private RepositoryClone repositoryCloneMock

    def setup() {
        commitHashPersistenceServiceMock = Mock CommitHashPersistenceService
        gitHubRepositoryCommitsToPersistFinderMock = Mock GitHubRepositoryCommitsToPersistFinder
        repositoriesClonesDAOMock = Mock RepositoriesClonesDAO
        dateFactoryMock = new DateFactory(new NowDateProvider() {
            @Override
            public Date createNowDate() {
                return NOW
            }
        })

        repositoryCloneMock = Mock RepositoryClone

        gitHubRepositoryClonePersister = new GitHubRepositoryClonePersister(commitHashPersistenceServiceMock,
                gitHubRepositoryCommitsToPersistFinderMock, repositoriesClonesDAOMock, dateFactoryMock)

    }

    def "persist when there is no repository clones to process"() {
        given:
        repositoriesClonesDAOMock.findRepositoryCloneWithCommitsToPersist() >> null

        when:
        gitHubRepositoryClonePersister.persist()

        then:
        0 * gitHubRepositoryCommitsToPersistFinderMock.getCommitHashesToPersist(null) >> null
    }

    def "persist when there are error while retrieving hashes"() {
        given:
        repositoriesClonesDAOMock.findRepositoryCloneWithCommitsToPersist() >> repositoryCloneMock
        gitHubRepositoryCommitsToPersistFinderMock.getCommitHashesToPersist(repositoryCloneMock) >> null

        when:
        gitHubRepositoryClonePersister.persist()

        then:
        0 * repositoriesClonesDAOMock.update(*_) >> null
        0 * repositoriesClonesDAOMock.update(null) >> null
    }

    def "sets commitsStatisticsUntil field to the beginning o not yet opened month, when commit list is empty"() {
        given:
        repositoriesClonesDAOMock.findRepositoryCloneWithCommitsToPersist() >> repositoryCloneMock
        gitHubRepositoryCommitsToPersistFinderMock.getCommitHashesToPersist(repositoryCloneMock) >> Lists.newArrayList()

        when:
        gitHubRepositoryClonePersister.persist()

        then:
        1 * repositoryCloneMock.setCommitsStatisticsUntil(_ as Date) >> { date ->
            assert date != null
        }
        1 * repositoriesClonesDAOMock.update(repositoryCloneMock)
    }

}
