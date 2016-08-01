package com.cezarykluczynski.carmen.executor.github

import com.beust.jcommander.internal.Lists
import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAO as PropagationsRepositoriesDAO
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserRepositoriesPropagation
import com.cezarykluczynski.carmen.set.github.Repository as RepositorySet
import com.google.common.collect.Maps
import org.springframework.beans.factory.annotation.Autowired

class RepositoriesExecutorTest extends IntegrationTest {

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private RepositoriesDAO githubRepositoriesDAOImpl

    @Autowired
    private PropagationsRepositoriesDAO propagationsRepositoriesDAOImpl

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

        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity
        userRepositoriesPropagation.propagate()

        userEntityLogin = userEntity.getLogin()
        mockRepositoryFullName = "${userEntityLogin}/mockRepository"

        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "Repositories"
        pathParams = Maps.newHashMap()
        pathParams.put("login", userEntityLogin)
        pendingRequestEntity.setPathParams pathParams

        repositoriesSetList = Lists.newArrayList()
        RepositorySet repositorySet = Mock RepositorySet
        repositorySet.getFullName() >> mockRepositoryFullName
        repositoriesSetList.add repositorySet
    }

    void cleanup() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "executes"() {
        when:
        repositoriesExecutor.execute pendingRequestEntity

        then:
        githubRepositoriesDAOImpl.findByUser(userEntity).get(0).getFullName() == mockRepositoryFullName
        propagationsRepositoriesDAOImpl.findByUser(userEntity).getPhase() == "sleep"
        1 * githubClientMock.getRepositories(userEntityLogin) >> repositoriesSetList
    }

}
