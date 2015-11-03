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
import com.cezarykluczynski.carmen.provider.github.GithubProvider

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
    GithubProvider githubProvider

    @Test
    void createOrUpdateExistingRequestedEntityThatCannotBeUpdated() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        String currentLogin = userEntity.getLogin()
        UserSet userSet = new UserSet(null, currentLogin)
        GithubProvider githubProviderMock = getGithubProviderMock()
        when githubProviderMock.getUser(currentLogin) thenReturn userSet
        githubUserDAOImpl.setGithubProvider githubProviderMock

        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin currentLogin
        verify(githubProviderMock, never()).getUser currentLogin
        Assert.assertEquals userEntityUpdated.getLogin(), currentLogin

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntityByUserEntity userEntity
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        githubUserDAOImpl.setGithubProvider githubProvider
    }

    @Test
    void createOrUpdateExistingRequestedEntityThatCanBeUpdated() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSetMock = new UserSet(null, newLogin)
        GithubProvider githubProviderMock = getGithubProviderMock()
        when githubProviderMock.getUser(currentLogin) thenReturn userSetMock
        githubUserDAOImpl.setGithubProvider githubProviderMock

        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertTrue userEntityUpdated instanceof User
        Assert.assertEquals userEntityUpdated.getLogin(), newLogin
        verify(githubProviderMock).getUser currentLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        githubUserDAOImpl.setGithubProvider githubProvider
    }

    @Test
    void createOrUpdateExistingGhostEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundGhostUserEntity()
        setUserEntityUpdatedDateToTwoDaysAgo userEntity
        String currentLogin = userEntity.getLogin()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSet = new UserSet(null, newLogin)
        GithubProvider githubProviderMock = getGithubProviderMock()
        when githubProviderMock.getUser(currentLogin) thenReturn userSet
        githubUserDAOImpl.setGithubProvider githubProviderMock

        // exercise
        githubUserDAOImpl.createOrUpdateGhostEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertEquals userEntityUpdated.getRequested(), false
        Assert.assertEquals userEntityUpdated.getLogin(), newLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        githubUserDAOImpl.setGithubProvider githubProvider
    }

    @Test
    void createOrUpdateExistingGhostEntityWithExistingRequestedEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundGhostUserEntity()
        String currentLogin = userEntity.getLogin()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSet = new UserSet(null, newLogin)
        GithubProvider githubProviderMock = getGithubProviderMock()
        when githubProviderMock.getUser(currentLogin) thenReturn userSet
        githubUserDAOImpl.setGithubProvider githubProviderMock

        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertEquals userEntityUpdated.getRequested(), true
        Assert.assertEquals userEntityUpdated.getLogin(), newLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        githubUserDAOImpl.setGithubProvider githubProvider
    }

    @Test
    void createOrUpdateNonExistingEntity() {
        // setup
        String currentLogin = githubUserDAOImplFixtures.generateRandomLogin()
        GithubProvider githubProviderMock = getGithubProviderMock()
        UserSet userSetMock = new UserSet(null, currentLogin)
        when githubProviderMock.getUser(currentLogin) thenReturn userSetMock
        githubUserDAOImpl.setGithubProvider githubProviderMock

        // exercise
        User userEntityUpdated = githubUserDAOImpl.createOrUpdateRequestedEntity currentLogin

        // assertion
        Assert.assertEquals userEntityUpdated.getLogin(), currentLogin

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntityUpdated
        githubUserDAOImpl.setGithubProvider githubProvider
    }

    private GithubProvider getGithubProviderMock() {
        GithubProvider githubProviderMock = mock GithubProvider.class
        doNothing().when(githubProviderMock).checkApiLimit "getUser"
        doNothing().when(githubProviderMock).decrementRateLimitRemainingCounter "getUser"
        return githubProviderMock
    }

    private void setUserEntityUpdatedDateToTwoDaysAgo(User userEntity) {
        MutableDateTime twoDaysAgo = new MutableDateTime()
        twoDaysAgo.addDays(-2)
        Date twoDaysAgoDate = twoDaysAgo.toDate()
        userEntity.setUpdated twoDaysAgoDate
        githubUserDAOImpl.update userEntity
    }

}
