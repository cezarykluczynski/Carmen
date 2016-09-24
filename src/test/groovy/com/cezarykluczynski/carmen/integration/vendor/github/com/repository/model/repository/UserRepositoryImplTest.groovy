package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.set.github.UserDTO as UserSet
import com.cezarykluczynski.carmen.util.db.TransactionalExecutor
import org.joda.time.MutableDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

import javax.persistence.EntityManager

class UserRepositoryImplTest extends IntegrationTest {

    private UserRepositoryImpl userRepositoryImpl

    @Autowired
    private UserRepository userRepository

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserFollowersRepositoryFixtures userFollowersRepositoryFixtures

    @Autowired
    private UserFollowingRepositoryFixtures userFollowingRepositoryFixtures

    GithubClient githubClientMock

    @Autowired
    private ApplicationContext ctx

    def setup() {
        githubClientMock = getGithubClientMock()
        userRepositoryImpl = new UserRepositoryImpl(ctx.getBean(EntityManager.class), githubClientMock,
                 ctx.getBean(TransactionalExecutor.class))
        userRepositoryImpl.userRepository = userRepository
    }

    def "creates or updates existing requested entity that cannot be updated"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        String currentLogin = userEntity.getLogin()

        when:
        userRepositoryImpl.createOrUpdateRequestedEntity currentLogin
        User userEntityUpdated = userRepository.findByLogin currentLogin

        then:
        0 * githubClientMock.getUser(currentLogin)
        userEntityUpdated.getLogin() == currentLogin

        cleanup:
        userFollowersRepositoryFixtures.deleteUserFollowersEntityByUserEntity userEntity
        userFollowingRepositoryFixtures.deleteUserFollowingEntityByUserEntity userEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "creates or updates existing requested entity that can be updated"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = userRepositoryFixtures.generateRandomLogin()
        UserSet userSetMock = UserSet.builder().login(newLogin).build()

        when:
        userRepositoryImpl.createOrUpdateRequestedEntity currentLogin
        User userEntityUpdated = userRepository.findByLogin newLogin

        then:
        1 * githubClientMock.getUser(currentLogin) >> userSetMock
        userEntityUpdated instanceof User
        userEntityUpdated.getLogin() == newLogin

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "creates or updates existing ghost entity"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundGhostUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = userRepositoryFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()
        githubClientMock.getUser(currentLogin) >> userSet

        when:
        userRepositoryImpl.createOrUpdateGhostEntity currentLogin
        User userEntityUpdated = userRepository.findByLogin newLogin

        then:
        !userEntityUpdated.isRequested()
        userEntityUpdated.getLogin() == newLogin

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "creates or updates existing ghost entity with existing requested entity"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundGhostUserEntity()
        String currentLogin = userEntity.getLogin()
        String newLogin = userRepositoryFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()
        githubClientMock.getUser(currentLogin) >> userSet

        when:
        userRepositoryImpl.createOrUpdateRequestedEntity currentLogin
        User userEntityUpdated = userRepository.findByLogin newLogin

        then:
        userEntityUpdated.isRequested()
        userEntityUpdated.getLogin() == newLogin

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "creates or updates non-existing entity"() {
        given:
        String currentLogin = userRepositoryFixtures.generateRandomLogin()
        GithubClient githubClientMock = getGithubClientMock()
        UserSet userSetMock = UserSet.builder().login(currentLogin).build()
        githubClientMock.getUser(currentLogin) >> userSetMock

        when:
        User userEntityUpdated = userRepository.createOrUpdateRequestedEntity currentLogin

        then:
        userEntityUpdated.getLogin() == currentLogin

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntityUpdated
    }

    private GithubClient getGithubClientMock() {
        return Mock(GithubClient)
    }

    private void setUserEntityUpdatedDateToTwoDaysAgo(User userEntity) {
        MutableDateTime twoDaysAgo = new MutableDateTime()
        twoDaysAgo.addDays(-2)
        Date twoDaysAgoDate = twoDaysAgo.toDate()
        userEntity.setUpdated twoDaysAgoDate
        userRepository.save userEntity
    }

}
