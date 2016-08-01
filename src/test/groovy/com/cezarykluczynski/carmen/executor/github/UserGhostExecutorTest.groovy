package com.cezarykluczynski.carmen.executor.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import com.google.common.collect.Maps
import org.springframework.beans.factory.annotation.Autowired

class UserGhostExecutorTest extends IntegrationTest {

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserGhostExecutor userGhostExecutor

    private UserDAOImpl githubUserDAOImpl

    private User userEntity1

    private User userEntity2

    private PendingRequest pendingRequestEntity

    private HashMap<String, Object> params

    private HashMap<String, Object> pathParams

    def setup() {
        githubUserDAOImpl = Mock UserDAOImpl
        userGhostExecutor.githubUserDAOImpl = githubUserDAOImpl

        userEntity1 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userEntity2 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "UsersGhost"

        params = Maps.newHashMap()
        params.put("link_with", userEntity2.getId())

        pathParams = new HashMap<String, Object>()
        pathParams.put("login", userEntity1.getLogin())
        pendingRequestEntity.setPathParams pathParams
    }

    void cleanup() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity1
        githubUserDAOImplFixtures.deleteUserEntity userEntity2
    }

    def "executes follower"() {
        given:
        params.put("link_as", "follower")
        pendingRequestEntity.setParams params
        githubUserDAOImpl.createOrUpdateGhostEntity(userEntity1.getLogin()) >> userEntity1
        githubUserDAOImpl.findById(userEntity2.getId().intValue()) >> userEntity2

        when:
        userGhostExecutor.execute pendingRequestEntity

        then:
        1 * githubUserDAOImpl.linkFollowerWithFollowee(userEntity1, userEntity2)
    }

    def "executes followee"() {
        given:
        params.put("link_as", "followee")
        pendingRequestEntity.setParams params
        pendingRequestEntity.setPathParams pathParams
        githubUserDAOImpl.createOrUpdateGhostEntity(userEntity1.getLogin()) >> userEntity1
        githubUserDAOImpl.findById(userEntity2.getId().intValue()) >> userEntity2

        when:
        userGhostExecutor.execute pendingRequestEntity

        then:
        1 * githubUserDAOImpl.linkFollowerWithFollowee(userEntity2, userEntity1)
    }

}
