package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubEgitClient
import com.cezarykluczynski.carmen.set.github.Repository
import com.cezarykluczynski.carmen.set.github.UserDTO
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList
import org.springframework.beans.factory.annotation.Autowired

import java.util.stream.Collectors

class GithubEgitClientTest extends IntegrationTest {

    @Autowired
    private GithubEgitClient githubEgitClient

    def "getCoreLimit throws exception"() {
        when:
        githubEgitClient.getCoreLimit()

        then:
        IOException ex = thrown()
        ex.message == "Implemented in different provider."
    }

    def "getSearchLimit throws exception"() {
        when:
        githubEgitClient.getSearchLimit()

        then:
        IOException ex = thrown()
        ex.message == "Implemented in different provider."
    }

    def "getUser throws exception"() {
        when:
        githubEgitClient.getUser("login")

        then:
        IOException ex = thrown()
        ex.message == "Implemented in different provider."
    }

    def "gets repositories"() {
        when:
        List<Repository> repositoriesList = githubEgitClient.getRepositories "cezarykluczynski"

        then:
        repositoriesList.stream().map({repository -> repository.name}).collect(Collectors.toList()).contains("Carmen")
    }

    def "gets followers"() {
        when:
        PaginationAwareArrayList<UserDTO> followersList = githubEgitClient.getFollowers "octocat", 10, 0

        then:
        followersList.size() == 10
        followersList.get(0).getLogin() != null
    }

    def "gets following"() {
        when:
        PaginationAwareArrayList<UserDTO> followingList = githubEgitClient.getFollowing "octocat", 10, 0

        then:
        // assertion: last time checked, Octocat was following 6 GitHub employees
        followingList.size() > 5
        followingList.get(0).getLogin() != null
    }

}
