package com.cezarykluczynski.carmen.executor.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.google.common.collect.Maps
import org.springframework.beans.factory.annotation.Autowired

class UserGhostExecutorTest extends IntegrationTest {

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserGhostExecutor userGhostExecutor

    private UserRepository userRepository

    private User userEntity1

    private User userEntity2

    private PendingRequest pendingRequestEntity

    private HashMap<String, Object> params

    private HashMap<String, Object> pathParams

    def setup() {
        userRepository = Mock UserRepository
        userGhostExecutor.userRepository = userRepository

        userEntity1 = userRepositoryFixtures.createFoundRequestedUserEntity()
        userEntity2 = userRepositoryFixtures.createFoundRequestedUserEntity()

        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "UsersGhost"
        pendingRequestEntity.setPriority 0

        params = Maps.newHashMap()
        params.put("link_with", userEntity2.getId())

        pathParams = new HashMap<String, Object>()
        pathParams.put("login", userEntity1.getLogin())
        pendingRequestEntity.setPathParams pathParams
    }

    void cleanup() {
        userRepositoryFixtures.deleteUserEntity userEntity1
        userRepositoryFixtures.deleteUserEntity userEntity2
    }

    def "executes follower"() {
        given:
        params.put("link_as", "follower")
        pendingRequestEntity.setParams params
        userRepository.createOrUpdateGhostEntity(userEntity1.getLogin()) >> userEntity1
        userRepository.findById(userEntity2.getId()) >> userEntity2

        when:
        userGhostExecutor.execute pendingRequestEntity

        then:
        1 * userRepository.linkFollowerWithFollowee(userEntity1, userEntity2)
    }

    def "executes followee"() {
        given:
        params.put("link_as", "followee")
        pendingRequestEntity.setParams params
        pendingRequestEntity.setPathParams pathParams
        userRepository.createOrUpdateGhostEntity(userEntity1.getLogin()) >> userEntity1
        userRepository.findById(userEntity2.getId()) >> userEntity2

        when:
        userGhostExecutor.execute pendingRequestEntity

        then:
        1 * userRepository.linkFollowerWithFollowee(userEntity2, userEntity1)
    }

}
