package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.client.github.GithubClient
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.BeforeMethod

import java.lang.reflect.Field

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.never
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.verify

import org.testng.annotations.Test
import org.testng.Assert

import org.joda.time.MutableDateTime

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class UserDAOImplCreateOrUpdateTest extends AbstractTestNGSpringContextTests {

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

    @BeforeMethod
    void setUp() {
        githubUserDAOImpl = new UserDAOImpl(sessionFactory, githubClient, githubUserDAOImplFollowersFolloweesLinkerDelegate)
    }

    @Test
    void createOrUpdateExistingRequestedEntityThatCannotBeUpdated() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        String currentLogin = userEntity.getLogin()
        UserSet userSet = UserSet.builder().login(currentLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        when githubClientMock.getUser(currentLogin) thenReturn userSet
        setGithubClientToDao githubUserDAOImpl, githubClientMock

        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin currentLogin
        verify(githubClientMock, never()).getUser currentLogin
        Assert.assertEquals userEntityUpdated.getLogin(), currentLogin

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntityByUserEntity userEntity
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    @Test
    void createOrUpdateExistingRequestedEntityThatCanBeUpdated() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSetMock = UserSet.builder().login(newLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        when githubClientMock.getUser(currentLogin) thenReturn userSetMock
        setGithubClientToDao githubUserDAOImpl, githubClientMock

        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertTrue userEntityUpdated instanceof User
        Assert.assertEquals userEntityUpdated.getLogin(), newLogin
        verify(githubClientMock).getUser currentLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    @Test
    void createOrUpdateExistingGhostEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundGhostUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        when githubClientMock.getUser(currentLogin) thenReturn userSet
        setGithubClientToDao githubUserDAOImpl, githubClientMock

        // exercise
        githubUserDAOImpl.createOrUpdateGhostEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertEquals userEntityUpdated.isRequested(), false
        Assert.assertEquals userEntityUpdated.getLogin(), newLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    @Test
    void createOrUpdateExistingGhostEntityWithExistingRequestedEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundGhostUserEntity()
        String currentLogin = userEntity.getLogin()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        when githubClientMock.getUser(currentLogin) thenReturn userSet
        setGithubClientToDao githubUserDAOImpl, githubClientMock

        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertEquals userEntityUpdated.isRequested(), true
        Assert.assertEquals userEntityUpdated.getLogin(), newLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    @Test
    void createOrUpdateNonExistingEntity() {
        // setup
        String currentLogin = githubUserDAOImplFixtures.generateRandomLogin()
        GithubClient githubClientMock = getGithubClientMock()
        UserSet userSetMock = UserSet.builder().login(currentLogin).build()
        when githubClientMock.getUser(currentLogin) thenReturn userSetMock
        setGithubClientToDao githubUserDAOImpl, githubClient

        // exercise
        User userEntityUpdated = githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        Assert.assertEquals userEntityUpdated.getLogin(), currentLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntityUpdated
        setGithubClientToDao githubUserDAOImpl, githubClient
    }

    private GithubClient getGithubClientMock() {
        GithubClient githubClientMock = mock GithubClient.class
        doNothing().when(githubClientMock).checkApiLimit "getUser"
        doNothing().when(githubClientMock).decrementRateLimitRemainingCounter "getUser"
        return githubClientMock
    }

    private void setUserEntityUpdatedDateToTwoDaysAgo(User userEntity) {
        MutableDateTime twoDaysAgo = new MutableDateTime()
        twoDaysAgo.addDays(-2)
        Date twoDaysAgoDate = twoDaysAgo.toDate()
        userEntity.setUpdated twoDaysAgoDate
        githubUserDAOImpl.update userEntity
    }

    private static void setGithubClientToDao(UserDAO githubUserDAOImpl, GithubClient githubClient) {
        Field field = githubUserDAOImpl.getClass().getDeclaredField "githubClient"
        field.setAccessible true
        field.set githubUserDAOImpl, githubClient
    }

}
