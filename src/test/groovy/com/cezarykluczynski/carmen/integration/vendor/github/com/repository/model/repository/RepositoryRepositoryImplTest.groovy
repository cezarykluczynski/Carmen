package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository as GitHubRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.set.github.RepositoryDTO
import com.google.common.collect.Lists
import spock.lang.Specification

class RepositoryRepositoryImplTest extends Specification {

    private RepositoryRepository repositoryRepositoryMock

    private RepositoryRepositoryRefresherDelegate repositoryRepositoryRefresherDelegate

    private RepositoryRepositoryImpl repositoryRepositoryImpl

    def setup() {
        repositoryRepositoryMock = Mock(RepositoryRepository)
        repositoryRepositoryRefresherDelegate = Mock(RepositoryRepositoryRefresherDelegate)
        repositoryRepositoryImpl = new RepositoryRepositoryImpl(repositoryRepositoryRefresherDelegate,
                repositoryRepositoryMock)
    }

    def "should call delegate with list of GitHub repositories"() {
        given:
        User user = Mock(User)
        List<GitHubRepository> gitHubRepositories = Lists.newArrayList()
        List<RepositoryDTO> repositories = Lists.newArrayList()

        when:
        repositoryRepositoryImpl.refresh(user, repositories)

        then:
        1 * repositoryRepositoryMock.findByUser(user) >> gitHubRepositories
        1 * repositoryRepositoryRefresherDelegate.refresh(user, repositories, gitHubRepositories)
    }

}