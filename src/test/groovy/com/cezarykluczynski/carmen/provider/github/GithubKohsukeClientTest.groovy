package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.client.github.GithubClientInterface
import com.cezarykluczynski.carmen.client.github.GithubKohsukeClient
import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager
import spock.lang.Specification

class GithubKohsukeClientTest extends Specification {

    private GithubKohsukeClient githubKohsukeClient

    def setup() {
        githubKohsukeClient = new GithubKohsukeClient(null)
    }

    def "getCoreLimit throws exception"() {
        when:
        githubKohsukeClient.getCoreLimit()

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "getSearchLimit throws exception"() {
        when:
        githubKohsukeClient.getSearchLimit()

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "getUser throws exception"() {
        when:
        githubKohsukeClient.getUser "name"

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "getRepositories throws exception"() {
        when:
        githubKohsukeClient.getRepositories "name"

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "getFollowers throws exception"() {
        when:
        githubKohsukeClient.getFollowers "name", new Pager()

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

    def "getFollowing throws exception"() {
        when:
        githubKohsukeClient.getFollowing "name", new Pager()

        then:
        IOException ex = thrown()
        ex.message == GithubClientInterface.NOT_IMPLEMENTED
    }

}
