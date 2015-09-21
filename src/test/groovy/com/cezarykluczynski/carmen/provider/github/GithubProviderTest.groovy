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

import com.cezarykluczynski.carmen.set.github.User
import com.cezarykluczynski.carmen.set.github.RateLimit
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList
import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImpl

import java.io.IOException
import java.util.Iterator

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
class GithubProviderTest extends AbstractTestNGSpringContextTests {

    GithubProvider githubProvider

    GithubJcabiProvider githubJcabiProviderMock

    GithubEgitProvider githubEgitProviderMock

    GithubKohsukeProvider githubKohsukeProviderMock

    RateLimitDAOImpl rateLimitDAOImplMock

    RateLimit rateLimitSetCore

    RateLimit rateLimitSetSearch

    @BeforeMethod
    void setUp() {
        githubJcabiProviderMock = mock GithubJcabiProvider.class
        githubEgitProviderMock = mock GithubEgitProvider.class
        githubKohsukeProviderMock = mock GithubKohsukeProvider.class
        rateLimitDAOImplMock = mock RateLimitDAOImpl.class

        rateLimitSetCore = new RateLimit("core", 50, 45, new Date())
        rateLimitSetSearch = new RateLimit("search", 50, 45, new Date())

        when githubJcabiProviderMock.getCoreLimit() thenReturn rateLimitSetCore
        when githubJcabiProviderMock.getSearchLimit() thenReturn rateLimitSetSearch

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
        //
    }

    @Test
    void getFollowers() {
        //
    }

    @Test
    void getFollowing() {
        //
    }

}
