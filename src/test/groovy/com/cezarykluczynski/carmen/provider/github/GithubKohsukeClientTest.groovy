package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.client.github.GithubKohsukeClient
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
        ex.message == "Implemented in different provider."
    }

    def "getSearchLimit throws exception"() {
        when:
        githubKohsukeClient.getSearchLimit()

        then:
        IOException ex = thrown()
        ex.message == "Implemented in different provider."
    }

    def "getUser throws exception"() {
        when:
        githubKohsukeClient.getUser "name"

        then:
        IOException ex = thrown()
        ex.message == "Implemented in different provider."
    }

    def "getRepositories throws exception"() {
        when:
        githubKohsukeClient.getRepositories "name"

        then:
        IOException ex = thrown()
        ex.message == "Implemented in different provider."
    }

    def "getFollowers throws exception"() {
        when:
        githubKohsukeClient.getFollowers "name", 1, 0

        then:
        IOException ex = thrown()
        ex.message == "Implemented in different provider."
    }

    def "getFollowing throws exception"() {
        when:
        githubKohsukeClient.getFollowing "name", 1, 0

        then:
        IOException ex = thrown()
        ex.message == "Implemented in different provider."
    }

}
