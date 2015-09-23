package com.cezarykluczynski.carmen.provider.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.Test
import org.testng.annotations.BeforeMethod
import org.testng.Assert

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify

import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.set.github.RateLimit
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList
import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures

import java.io.IOException
import java.util.Iterator

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class GithubProviderTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    GithubProvider githubProvider

    GithubJcabiProvider githubJcabiProviderMock

    GithubEgitProvider githubEgitProviderMock

    GithubKohsukeProvider githubKohsukeProviderMock

    RateLimitDAOImpl rateLimitDAOImplMock

    RateLimit rateLimitSetCore

    RateLimit rateLimitSetSearch

    UserSet userSet

    String login

    PaginationAwareArrayList followersList

    PaginationAwareArrayList followingList

    @BeforeMethod
    void setUp() {
        githubJcabiProviderMock = mock GithubJcabiProvider.class
        githubEgitProviderMock = mock GithubEgitProvider.class
        githubKohsukeProviderMock = mock GithubKohsukeProvider.class
        rateLimitDAOImplMock = mock RateLimitDAOImpl.class
        userSet = mock UserSet.class
        rateLimitSetCore = mock RateLimit.class
        rateLimitSetSearch = mock RateLimit.class
        followersList = mock PaginationAwareArrayList.class
        followingList = mock PaginationAwareArrayList.class

        login = githubUserDAOImplFixtures.getRandomLogin()

        when githubJcabiProviderMock.getCoreLimit() thenReturn rateLimitSetCore
        when githubJcabiProviderMock.getSearchLimit() thenReturn rateLimitSetSearch
        when githubJcabiProviderMock.getUser(login) thenReturn userSet
        when githubEgitProviderMock.getFollowers(login, 1, 0) thenReturn followersList
        when githubEgitProviderMock.getFollowing(login, 1, 0) thenReturn followingList

        githubProvider = new GithubProvider(
            githubJcabiProviderMock,
            githubKohsukeProviderMock,
            githubEgitProviderMock,
            rateLimitDAOImplMock
        )
    }

    @Test
    void getCoreLimit() {
        // exercise
        RateLimit rateLimitSetCoreReturned = githubProvider.getCoreLimit()

        // assertion
        verify(githubJcabiProviderMock).getCoreLimit()
        Assert.assertEquals rateLimitSetCoreReturned, rateLimitSetCore
    }

    @Test
    void getSearchLimit() {
        // exercise
        RateLimit rateLimitSetSearchReturned = githubProvider.getSearchLimit()

        // assertion
        verify(githubJcabiProviderMock).getSearchLimit()
        Assert.assertEquals rateLimitSetSearchReturned, rateLimitSetSearch
    }

    @Test
    void getUser() {
        // exercise
        UserSet userSetReturned = githubProvider.getUser(login)

        // assertion
        verify(githubJcabiProviderMock).getUser(login)
        Assert.assertEquals userSetReturned, userSet
    }

    @Test
    void getFollowers() {
        // exercise
        PaginationAwareArrayList<UserSet> followersListReturned = githubProvider.getFollowers(login, 1, 0)

        // assertion
        verify(githubEgitProviderMock).getFollowers(login, 1, 0)
        Assert.assertEquals followersListReturned, followersList
    }

    @Test
    void getFollowing() {
        // exercise
        PaginationAwareArrayList<UserSet> followingListReturned = githubProvider.getFollowing(login, 1, 0)

        // assertion
        verify(githubEgitProviderMock).getFollowing(login, 1, 0)
        Assert.assertEquals followingListReturned, followingList
    }

}
