package com.cezarykluczynski.carmen.provider.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.Test
import org.testng.Assert

import com.cezarykluczynski.carmen.set.github.Repository
import com.cezarykluczynski.carmen.set.github.User
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList

import java.io.IOException
import java.util.Iterator

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
class GithubEgitProviderIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GithubEgitProvider githubEgitProvider

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getCoreLimit() {
        githubEgitProvider.getCoreLimit()
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getSearchLimit() {
        githubEgitProvider.getSearchLimit()
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Implemented in different provider.")
    void getUser() {
        githubEgitProvider.getUser()
    }

    @Test
    void getRepositories() {
        List<Repository> repositoriesList = githubEgitProvider.getRepositories "cezarykluczynski"
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
        PaginationAwareArrayList<User> followersList = githubEgitProvider.getFollowers "octocat", 10, 0

        // assertion
        Assert.assertEquals followersList.size(), 10

        User userSet = followersList.get 0
        Assert.assertNotNull userSet.getLogin()
    }

    @Test
    void getFollowing() {
        // exercise
        PaginationAwareArrayList<User> followingList = githubEgitProvider.getFollowing "octocat", 10, 0

        // assertion: last time checked, Octocat was following 6 GitHub employees
        Assert.assertTrue followingList.size() > 5

        User userSet = followingList.get 0
        Assert.assertNotNull userSet.getLogin()
    }

}
