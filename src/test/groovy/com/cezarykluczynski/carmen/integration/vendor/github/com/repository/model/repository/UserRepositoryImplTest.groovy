package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.set.github.UserDTO as UserSet
import org.joda.time.MutableDateTime
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryImplTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository

    @Autowired
    private UserRepositoryImpl userRepositoryImpl

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserFollowersRepositoryFixtures userFollowersRepositoryFixtures

    @Autowired
    private UserFollowingRepositoryFixtures userFollowingRepositoryFixtures

    @Autowired
    private GithubClient githubClient


    def "creates or updates existing requested entity that cannot be updated"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        String currentLogin = userEntity.getLogin()
        GithubClient githubClientMock = getGithubClientMock()
        setGithubClientToDao userRepositoryImpl, githubClientMock

        when:
        userRepository.createOrUpdateRequestedEntity currentLogin
        User userEntityUpdated = userRepository.findByLogin currentLogin

        then:
        0 * githubClientMock.getUser(currentLogin)
        userEntityUpdated.getLogin() == currentLogin

        cleanup:
        userFollowersRepositoryFixtures.deleteUserFollowersEntityByUserEntity userEntity
        userFollowingRepositoryFixtures.deleteUserFollowingEntityByUserEntity userEntity
        userRepositoryFixtures.deleteUserEntity userEntity
        setGithubClientToDao userRepositoryImpl, githubClient
    }

    def "creates or updates existing requested entity that can be updated"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = userRepositoryFixtures.generateRandomLogin()
        UserSet userSetMock = UserSet.builder().login(newLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        setGithubClientToDao userRepositoryImpl, githubClientMock

        when:
        userRepository.createOrUpdateRequestedEntity currentLogin
        User userEntityUpdated = userRepository.findByLogin newLogin

        then:
        1 * githubClientMock.getUser(currentLogin) >> userSetMock
        userEntityUpdated instanceof User
        userEntityUpdated.getLogin() == newLogin

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
        setGithubClientToDao userRepositoryImpl, githubClient
    }

    def "creates or updates existing ghost entity"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundGhostUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = userRepositoryFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        githubClientMock.getUser(currentLogin) >> userSet
        setGithubClientToDao userRepositoryImpl, githubClientMock

        when:
        userRepository.createOrUpdateGhostEntity currentLogin
        User userEntityUpdated = userRepository.findByLogin newLogin

        then:
        !userEntityUpdated.isRequested()
        userEntityUpdated.getLogin() == newLogin

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
        setGithubClientToDao userRepositoryImpl, githubClient
    }

    def "creates or updates existing ghost entity with existing requested entity"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundGhostUserEntity()
        String currentLogin = userEntity.getLogin()
        String newLogin = userRepositoryFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        githubClientMock.getUser(currentLogin) >> userSet
        setGithubClientToDao userRepositoryImpl, githubClientMock

        when:
        userRepository.createOrUpdateRequestedEntity currentLogin
        User userEntityUpdated = userRepository.findByLogin newLogin

        then:
        userEntityUpdated.isRequested()
        userEntityUpdated.getLogin() == newLogin

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
        setGithubClientToDao userRepositoryImpl, githubClient
    }

    def "creates or updates non-existing entity"() {
        given:
        String currentLogin = userRepositoryFixtures.generateRandomLogin()
        GithubClient githubClientMock = getGithubClientMock()
        UserSet userSetMock = UserSet.builder().login(currentLogin).build()
        githubClientMock.getUser(currentLogin) >> userSetMock
        setGithubClientToDao userRepositoryImpl, githubClient

        when:
        User userEntityUpdated = userRepository.createOrUpdateRequestedEntity currentLogin

        then:
        userEntityUpdated.getLogin() == currentLogin

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntityUpdated
        setGithubClientToDao userRepositoryImpl, githubClient
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

    private static void setGithubClientToDao(UserRepositoryImpl userRepository, GithubClient githubClient) {
        userRepository.githubClient = githubClient
    }

}
