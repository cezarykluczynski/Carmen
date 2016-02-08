package com.cezarykluczynski.carmen.dao.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.client.github.GithubClient

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.times
import static org.mockito.Mockito.never
import static org.mockito.Mockito.thenReturn
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

import org.joda.time.MutableDateTime

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserDAOImplCreateOrUpdateTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    RateLimitDAOImplFixtures githubRateLimitDAOImplFixtures

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    UserDAO githubUserDAOImpl

    @Autowired
    GithubClient githubClient

    @Test
    void createOrUpdateExistingRequestedEntityThatCannotBeUpdated() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        String currentLogin = userEntity.getLogin()
        UserSet userSet = UserSet.builder().login(currentLogin).build()
        GithubClient githubClientMock = getGithubClientMock()
        when githubClientMock.getUser(currentLogin) thenReturn userSet
        githubUserDAOImpl.setGithubClient githubClientMock

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
        githubUserDAOImpl.setGithubClient githubClient
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
        githubUserDAOImpl.setGithubClient githubClientMock

        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertTrue userEntityUpdated instanceof User
        Assert.assertEquals userEntityUpdated.getLogin(), newLogin
        verify(githubClientMock).getUser currentLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        githubUserDAOImpl.setGithubClient githubClient
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
        githubUserDAOImpl.setGithubClient githubClientMock

        // exercise
        githubUserDAOImpl.createOrUpdateGhostEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertEquals userEntityUpdated.getRequested(), false
        Assert.assertEquals userEntityUpdated.getLogin(), newLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        githubUserDAOImpl.setGithubClient githubClient
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
        githubUserDAOImpl.setGithubClient githubClientMock

        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertEquals userEntityUpdated.getRequested(), true
        Assert.assertEquals userEntityUpdated.getLogin(), newLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        githubUserDAOImpl.setGithubClient githubClient
    }

    @Test
    void createOrUpdateNonExistingEntity() {
        // setup
        String currentLogin = githubUserDAOImplFixtures.generateRandomLogin()
        GithubClient githubClientMock = getGithubClientMock()
        UserSet userSetMock = UserSet.builder().login(currentLogin).build()
        when githubClientMock.getUser(currentLogin) thenReturn userSetMock
        githubUserDAOImpl.setGithubClient githubClientMock

        // exercise
        User userEntityUpdated = githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        Assert.assertEquals userEntityUpdated.getLogin(), currentLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntityUpdated
        githubUserDAOImpl.setGithubClient githubClient
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

}
