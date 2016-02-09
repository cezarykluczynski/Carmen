package com.cezarykluczynski.carmen.client.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.Test
import org.testng.Assert

import com.cezarykluczynski.carmen.set.github.User
import com.cezarykluczynski.carmen.set.github.RateLimit
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList

import java.io.IOException
import java.util.Iterator
import java.util.Date

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
class GithubJcabiClientIntegrationTest extends AbstractTestNGSpringContextTests {

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

    @Test(enabled = false)
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

    @Test(enabled = false)
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
