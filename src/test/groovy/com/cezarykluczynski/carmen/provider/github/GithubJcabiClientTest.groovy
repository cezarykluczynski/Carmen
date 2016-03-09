package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.client.github.GithubJcabiClient
import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.Test
import org.testng.Assert

import com.cezarykluczynski.carmen.set.github.User
import com.cezarykluczynski.carmen.set.github.RateLimit

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class GithubJcabiClientTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GithubJcabiClient githubJcabiClient

    @Test
    void getCoreLimit() {
        RateLimit rateLimitSet = githubJcabiClient.getCoreLimit()

        Assert.assertEquals rateLimitSet.getResource(), "core"
        Assert.assertTrue rateLimitSet.getLimit() instanceof Integer
        Assert.assertTrue rateLimitSet.getRemaining() instanceof Integer
        Assert.assertTrue rateLimitSet.getReset() instanceof Date
    }

    @Test
    void getSearchLimit() {
        RateLimit rateLimitSet = githubJcabiClient.getSearchLimit()

        Assert.assertEquals rateLimitSet.getResource(), "search"
        Assert.assertTrue rateLimitSet.getLimit() instanceof Integer
        Assert.assertTrue rateLimitSet.getRemaining() instanceof Integer
        Assert.assertTrue rateLimitSet.getReset() instanceof Date
    }

    @Test
    void getExistingUser() {
        User userSet = githubJcabiClient.getUser "octocat"

        Assert.assertNotNull userSet.getId()
        Assert.assertEquals userSet.getLogin(), "octocat"
        Assert.assertEquals userSet.getCompany(), "GitHub"
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getRepositories() {
        githubJcabiClient.getRepositories "name"
    }

    @Test
    void getNonExistingUser() {
        User userSet = githubJcabiClient.getUser "carmen-user-404-integration-test"

        Assert.assertNull userSet.getId()
        Assert.assertEquals userSet.getLogin(), "carmen-user-404-integration-test"
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getFollowers() {
        githubJcabiClient.getFollowers "name", 1, 0
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getFollowing() {
        githubJcabiClient.getFollowing "name", 1, 0
    }

}
