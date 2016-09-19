package com.cezarykluczynski.carmen.vcs.git.persistence.vendor.github

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.RepositoryClone
import com.cezarykluczynski.carmen.util.DateUtil
import com.cezarykluczynski.carmen.util.factory.DateFactory
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash
import com.cezarykluczynski.carmen.vcs.server.Server
import spock.lang.Specification

import java.time.LocalDateTime

class GitHubRepositoryCommitsToPersistFinderTest extends Specification {

    private DateFactory dateFactoryMock

    private Server serverMock

    private GitHubRepositoryCommitsToPersistFinder gitHubRepositoryCommitsToPersistFinder

    def setup() {
        dateFactoryMock = Mock DateFactory
        serverMock = Mock Server
        gitHubRepositoryCommitsToPersistFinder = new GitHubRepositoryCommitsToPersistFinder(dateFactoryMock, serverMock)
    }

    def "should check if repositoryClone is not null"() {
        when:
        gitHubRepositoryCommitsToPersistFinder.getCommitHashesToPersist null

        then:
        thrown NullPointerException
    }

    def "returns null when git command fails"() {
        given:
        RepositoryClone repositoryCloneMock = Mock RepositoryClone

        when:
        List<CommitHash> commitHashes =  gitHubRepositoryCommitsToPersistFinder.getCommitHashesToPersist(repositoryCloneMock)

        then:
        commitHashes == null
    }

    def "returns list of commit hashes for a given period"() {
        given:
        dateFactoryMock.getEndOfAlreadyClosedMonth() >> DateUtil.toDate(LocalDateTime.of(2016, 06, 30, 23, 59, 00))
        serverMock.getCloneRoot() >> '.'
        RepositoryClone repositoryCloneMock = Mock(RepositoryClone) {
            getLocationDirectory() >> '.'
            getLocationSubdirectory() >> '.'
            getCommitsStatisticsUntil() >> DateUtil.toDate(LocalDateTime.of(2016, 06, 01, 0, 0, 0))
        }

        when:
        List<CommitHash> commitHashes =  gitHubRepositoryCommitsToPersistFinder.getCommitHashesToPersist(repositoryCloneMock)

        then:
        commitHashes.size() == 27
        commitHashes[0].hash == '68a1c7cef5b4d44276d4096565f48d8b6d6b66e7'
        commitHashes[26].hash == 'c0d5fb5e8fa267c1b0f5b45eda7866dc5b65c578'
    }

}
