package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClientInterface
import com.cezarykluczynski.carmen.client.github.GithubJcabiClient
import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO
import com.cezarykluczynski.carmen.set.github.UserDTO
import org.springframework.beans.factory.annotation.Autowired

class GithubJcabiClientTest extends IntegrationTest {

    @Autowired
    private GithubJcabiClient githubJcabiClient

    def "gets core limit"() {
        when:
        RateLimitDTO rateLimitSet = githubJcabiClient.getCoreLimit()

        then:
        rateLimitSet.getResource() == "core"
        rateLimitSet.getLimit() instanceof Integer
        rateLimitSet.getRemaining() instanceof Integer
        rateLimitSet.getReset() instanceof Date
    }

    def "gets search limit"() {
        when:
        RateLimitDTO rateLimitSet = githubJcabiClient.getSearchLimit()

        then:
        rateLimitSet.getResource() == "search"
        rateLimitSet.getLimit() instanceof Integer
        rateLimitSet.getRemaining() instanceof Integer
        rateLimitSet.getReset() instanceof Date
    }

    def "gets existing user"() {
        when:
        UserDTO userSet = githubJcabiClient.getUser "octocat"

        then:
        userSet.getId() != null
        userSet.getLogin() == "octocat"
        userSet.getCompany() == "GitHub"
    }

    def "gets repositories throws exception"() {
        when:
        githubJcabiClient.getRepositories "name"

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "gets non existing user"() {
        when:
        UserDTO userSet = githubJcabiClient.getUser "carmen-user-404-integration-test"

        then:
        userSet.getId() == null
        userSet.getLogin() == "carmen-user-404-integration-test"
    }

    def "get followers throws exception"() {
        when:
        githubJcabiClient.getFollowers "name", new Pager()

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "get following throws exception"() {
        when:
        githubJcabiClient.getFollowing "name", new Pager()

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

}
