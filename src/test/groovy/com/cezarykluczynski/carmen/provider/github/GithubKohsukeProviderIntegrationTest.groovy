package com.cezarykluczynski.carmen.provider.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

import java.io.IOException

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
class GithubKohsukeProviderIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GithubKohsukeProvider githubKohsukeProvider

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getCoreLimit() {
        githubKohsukeProvider.getCoreLimit()
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getSearchLimit() {
        githubKohsukeProvider.getSearchLimit()
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getUser() {
        githubKohsukeProvider.getUser "name"
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getFollowers() {
        githubKohsukeProvider.getFollowers "name", 1, 0
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getFollowing() {
        githubKohsukeProvider.getFollowing "name", 1, 0
    }

}
