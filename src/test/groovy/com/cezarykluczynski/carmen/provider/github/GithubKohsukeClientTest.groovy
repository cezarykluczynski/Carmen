package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.client.github.GithubKohsukeClient
import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.Test

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class GithubKohsukeClientTest extends AbstractTestNGSpringContextTests {

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
