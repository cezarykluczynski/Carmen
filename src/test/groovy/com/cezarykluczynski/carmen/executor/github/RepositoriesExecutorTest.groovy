package com.cezarykluczynski.carmen.executor.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.RepositoriesRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.RepositoryRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest
import com.cezarykluczynski.carmen.propagation.github.UserRepositoriesPropagation
import com.cezarykluczynski.carmen.set.github.RepositoryDTO as RepositorySet
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import org.springframework.beans.factory.annotation.Autowired

class RepositoriesExecutorTest extends IntegrationTest {

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private RepositoryRepository repositoryRepository

    @Autowired
    private RepositoriesRepository repositoriesRepository

    @Autowired
    private UserRepositoriesPropagation userRepositoriesPropagation

    @Autowired
    private RepositoriesExecutor repositoriesExecutor

    private GithubClient githubClientMock

    private User userEntity

    private PendingRequest pendingRequestEntity

    private HashMap<String, Object> pathParams

    private List<RepositorySet> repositoriesSetList

    private String userEntityLogin

    private String mockRepositoryFullName

    void setup() {
        githubClientMock = Mock GithubClient
        repositoriesExecutor.githubClient = githubClientMock

        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity
        userRepositoriesPropagation.propagate()

        userEntityLogin = userEntity.getLogin()
        mockRepositoryFullName = "${userEntityLogin}/mockRepository"

        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "Repositories"
        pathParams = Maps.newHashMap()
        pathParams.put("login", userEntityLogin)
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setPriority 10

        repositoriesSetList = Lists.newArrayList()
        RepositorySet repositorySet = Mock RepositorySet
        repositorySet.getFullName() >> mockRepositoryFullName
        repositoriesSetList.add repositorySet
    }

    void cleanup() {
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "executes"() {
        when:
        repositoriesExecutor.execute pendingRequestEntity

        then:
        repositoryRepository.findByUser(userEntity).get(0).getFullName() == mockRepositoryFullName
        repositoriesRepository.findOneByUser(userEntity).getPhase() == "sleep"
        1 * githubClientMock.getRepositories(userEntityLogin) >> repositoriesSetList
    }

}
