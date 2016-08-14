package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet
import org.hibernate.SessionFactory
import org.joda.time.MutableDateTime
import org.springframework.beans.factory.annotation.Autowired

class UserDAOImplCreateOrUpdateTest extends IntegrationTest {

    private UserDAOImpl githubUserDAOImpl

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    private UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    UserDAOImplFollowersFolloweesLinkerDelegate githubUserDAOImplFollowersFolloweesLinkerDelegate

    @Autowired
    private GithubClient githubClient

    void setup() {
        githubUserDAOImpl = new UserDAOImpl(sessionFactory, githubClient, githubUserDAOImplFollowersFolloweesLinkerDelegate)
    }

    def "creates or updates existing requested entity that cannot be updated"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        String currentLogin = userEntity.getLogin()
        GithubClient githubClientMock = getGithubClientMock()
        setGithubClientToDao githubUserDAOImpl, githubClientMock

        when:
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin
        User userEntityUpdated = githubUserDAOImpl.findByLogin currentLogin

        then:
        0 * githubClientMock.getUser(currentLogin)
        userEntityUpdated.getLogin() == currentLogin

        cleanup:
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntityByUserEntity userEntity
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    def "creates or updates existing requested entity that can be updated"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSetMock = UserSet.builder().login(newLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        setGithubClientToDao githubUserDAOImpl, githubClientMock

        when:
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin

        then:
        1 * githubClientMock.getUser(currentLogin) >> userSetMock
        userEntityUpdated instanceof User
        userEntityUpdated.getLogin() == newLogin

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    def "creates or updates existing ghost entity"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundGhostUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        githubClientMock.getUser(currentLogin) >> userSet
        setGithubClientToDao githubUserDAOImpl, githubClientMock

        when:
        githubUserDAOImpl.createOrUpdateGhostEntity currentLogin
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin

        then:
        !userEntityUpdated.isRequested()
        userEntityUpdated.getLogin() == newLogin

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    def "creates or updates existing ghost entity with existing requested entity"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundGhostUserEntity()
        String currentLogin = userEntity.getLogin()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        githubClientMock.getUser(currentLogin) >> userSet
        setGithubClientToDao githubUserDAOImpl, githubClientMock

        when:
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin

        then:
        userEntityUpdated.isRequested()
        userEntityUpdated.getLogin() == newLogin

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    def "creates or updates non-existing entity"() {
        given:
        String currentLogin = githubUserDAOImplFixtures.generateRandomLogin()
        GithubClient githubClientMock = getGithubClientMock()
        UserSet userSetMock = UserSet.builder().login(currentLogin).build()
        githubClientMock.getUser(currentLogin) >> userSetMock
        setGithubClientToDao githubUserDAOImpl, githubClient

        when:
        User userEntityUpdated = githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        then:
        userEntityUpdated.getLogin() == currentLogin

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntityUpdated
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    private GithubClient getGithubClientMock() {
        return Mock(GithubClient)
    }

    private void setUserEntityUpdatedDateToTwoDaysAgo(User userEntity) {
        MutableDateTime twoDaysAgo = new MutableDateTime()
        twoDaysAgo.addDays(-2)
        Date twoDaysAgoDate = twoDaysAgo.toDate()
        userEntity.setUpdated twoDaysAgoDate
        githubUserDAOImpl.update userEntity
    }

    private static void setGithubClientToDao(UserDAOImpl githubUserDAOImpl, GithubClient githubClient) {
        githubUserDAOImpl.githubClient = githubClient
    }

}
