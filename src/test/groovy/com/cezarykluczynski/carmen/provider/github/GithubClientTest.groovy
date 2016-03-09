package com.cezarykluczynski.carmen.client.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.Test
import org.testng.annotations.BeforeMethod
import org.testng.Assert

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify

import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.set.github.RateLimit
import com.cezarykluczynski.carmen.set.github.Repository
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList
import com.cezarykluczynski.carmen.dao.github.RateLimitDAO
import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class GithubClientTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    GithubClient githubClient

    GithubJcabiClient githubJcabiClientMock

    GithubEgitClient githubEgitClientMock

    GithubKohsukeClient githubKohsukeClientMock

    RateLimitDAO rateLimitDAOImplMock

    RateLimit rateLimitSetCore

    RateLimit rateLimitSetSearch

    UserSet userSet

    String login

    PaginationAwareArrayList followersList

    PaginationAwareArrayList followingList

    List<Repository> repositoriesList

    @BeforeMethod
    void setUp() {
        githubJcabiClientMock = mock GithubJcabiClient.class
        githubEgitClientMock = mock GithubEgitClient.class
        githubKohsukeClientMock = mock GithubKohsukeClient.class
        rateLimitDAOImplMock = mock RateLimitDAOImpl.class
        rateLimitSetCore = mock RateLimit.class
        rateLimitSetSearch = mock RateLimit.class
        userSet = mock UserSet.class
        repositoriesList = mock List.class
        followersList = mock PaginationAwareArrayList.class
        followingList = mock PaginationAwareArrayList.class

        login = githubUserDAOImplFixtures.generateRandomLogin()

        when githubJcabiClientMock.getCoreLimit() thenReturn rateLimitSetCore
        when githubJcabiClientMock.getSearchLimit() thenReturn rateLimitSetSearch
        when githubJcabiClientMock.getUser(login) thenReturn userSet
        when githubEgitClientMock.getRepositories(login) thenReturn repositoriesList
        when githubEgitClientMock.getFollowers(login, 1, 0) thenReturn followersList
        when githubEgitClientMock.getFollowing(login, 1, 0) thenReturn followingList

        githubClient = new GithubClient(
            githubJcabiClientMock,
            githubKohsukeClientMock,
            githubEgitClientMock,
            rateLimitDAOImplMock
        )
    }

    @Test
    void getCoreLimit() {
        // exercise
        RateLimit rateLimitSetCoreReturned = githubClient.getCoreLimit()

        // assertion
        verify(githubJcabiClientMock).getCoreLimit()
        Assert.assertEquals rateLimitSetCoreReturned, rateLimitSetCore
    }

    @Test
    void getSearchLimit() {
        // exercise
        RateLimit rateLimitSetSearchReturned = githubClient.getSearchLimit()

        // assertion
        verify(githubJcabiClientMock).getSearchLimit()
        Assert.assertEquals rateLimitSetSearchReturned, rateLimitSetSearch
    }

    @Test
    void getUser() {
        // exercise
        UserSet userSetReturned = githubClient.getUser(login)

        // assertion
        verify(githubJcabiClientMock).getUser(login)
        Assert.assertEquals userSetReturned, userSet
    }

    @Test
    void getRepositories() {
        // exercise
        List<Repository> repositoriesListReturned = githubClient.getRepositories(login)

        // assertion
        verify(githubEgitClientMock).getRepositories(login)
        Assert.assertEquals repositoriesListReturned, repositoriesList
    }

    @Test
    void getFollowers() {
        // exercise
        PaginationAwareArrayList<UserSet> followersListReturned = githubClient.getFollowers(login, 1, 0)

        // assertion
        verify(githubEgitClientMock).getFollowers(login, 1, 0)
        Assert.assertEquals followersListReturned, followersList
    }

    @Test
    void getFollowing() {
        // exercise
        PaginationAwareArrayList<UserSet> followingListReturned = githubClient.getFollowing(login, 1, 0)

        // assertion
        verify(githubEgitClientMock).getFollowing(login, 1, 0)
        Assert.assertEquals followingListReturned, followingList
    }

}
