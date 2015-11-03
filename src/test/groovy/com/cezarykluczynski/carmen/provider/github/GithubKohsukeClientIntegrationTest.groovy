package com.cezarykluczynski.carmen.client.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.Test

import java.io.IOException

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
class GithubKohsukeClientIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GithubKohsukeClient githubKohsukeClient

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getCoreLimit() {
        githubKohsukeClient.getCoreLimit()
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getSearchLimit() {
        githubKohsukeClient.getSearchLimit()
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getUser() {
        githubKohsukeClient.getUser "name"
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getRepositories() {
        githubKohsukeClient.getRepositories "name"
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getFollowers() {
        githubKohsukeClient.getFollowers "name", 1, 0
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getFollowing() {
        githubKohsukeClient.getFollowing "name", 1, 0
    }

}
