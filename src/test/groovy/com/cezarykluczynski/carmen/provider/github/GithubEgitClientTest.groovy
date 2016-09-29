package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClientInterface
import com.cezarykluczynski.carmen.client.github.GithubEgitClient
import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager
import com.cezarykluczynski.carmen.common.util.pagination.dto.Slice
import com.cezarykluczynski.carmen.set.github.RepositoryDTO
import com.cezarykluczynski.carmen.set.github.UserDTO
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
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "getSearchLimit throws exception"() {
        when:
        githubEgitClient.getSearchLimit()

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "getUser throws exception"() {
        when:
        githubEgitClient.getUser("login")

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "gets repositories"() {
        when:
        List<RepositoryDTO> repositoriesList = githubEgitClient.getRepositories "cezarykluczynski"

        then:
        repositoriesList.stream().map({repository -> repository.name}).collect(Collectors.toList()).contains("Carmen")
    }

    def "gets followers"() {
        when:
        Slice<UserDTO> slice = githubEgitClient.getFollowers "octocat", new Pager(pageNumber: 0, itemsPerPage: 10)

        then:
        slice.getPage().size() == 10
        slice.getPage()[0].getLogin() != null
    }

    def "gets following"() {
        when:
        Slice<UserDTO> slice = githubEgitClient.getFollowing "octocat", new Pager(pageNumber: 0, itemsPerPage: 10)

        then:
        // assertion: last time checked, Octocat was following 6 GitHub employees
        slice.getPage().size() > 5
        slice.getPage()[0].getLogin() != null
    }

}
