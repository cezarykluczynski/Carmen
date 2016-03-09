package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.client.github.GithubEgitClient
import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.Test
import org.testng.Assert

import com.cezarykluczynski.carmen.set.github.Repository
import com.cezarykluczynski.carmen.set.github.User
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class GithubEgitClientTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GithubEgitClient githubEgitClient

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getCoreLimit() {
        githubEgitClient.getCoreLimit()
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getSearchLimit() {
        githubEgitClient.getSearchLimit()
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getUser() {
        githubEgitClient.getUser()
    }

    @Test
    void getRepositories() {
        List<Repository> repositoriesList = githubEgitClient.getRepositories "cezarykluczynski"
        for(Repository repository in repositoriesList) {
            if (repository.getName() == "Carmen") {
                Assert.assertTrue true
                return;
            }
        }

        Assert.fail()
    }

    @Test
    void getFollowers() {
        // exercise
        PaginationAwareArrayList<User> followersList = githubEgitClient.getFollowers "octocat", 10, 0

        // assertion
        Assert.assertEquals followersList.size(), 10

        User userSet = followersList.get 0
        Assert.assertNotNull userSet.getLogin()
    }

    @Test
    void getFollowing() {
        // exercise
        PaginationAwareArrayList<User> followingList = githubEgitClient.getFollowing "octocat", 10, 0

        // assertion: last time checked, Octocat was following 6 GitHub employees
        Assert.assertTrue followingList.size() > 5

        User userSet = followingList.get 0
        Assert.assertNotNull userSet.getLogin()
    }

}
