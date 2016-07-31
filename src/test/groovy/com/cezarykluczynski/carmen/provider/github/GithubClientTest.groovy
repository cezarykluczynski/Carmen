package com.cezarykluczynski.carmen.client.github

import com.cezarykluczynski.carmen.dao.github.RateLimitDAO
import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImpl
import com.cezarykluczynski.carmen.set.github.RateLimit
import com.cezarykluczynski.carmen.set.github.Repository
import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList
import spock.lang.Specification

class GithubClientTest extends Specification {

    private static final String LOGIN = "LOGIN"

    private GithubClient githubClient

    private GithubJcabiClient githubJcabiClientMock

    private GithubEgitClient githubEgitClientMock

    private GithubKohsukeClient githubKohsukeClientMock

    private RateLimitDAO rateLimitDAOImplMock

    private RateLimit rateLimitSetCore

    private RateLimit rateLimitSetSearch

    private UserSet userSet

    private PaginationAwareArrayList followersList

    private PaginationAwareArrayList followingList

    private List<Repository> repositoriesList

    def setup() {
        githubJcabiClientMock = Mock GithubJcabiClient
        githubEgitClientMock = Mock GithubEgitClient
        githubKohsukeClientMock = Mock GithubKohsukeClient
        rateLimitDAOImplMock = Mock RateLimitDAOImpl
        rateLimitSetCore = Mock RateLimit
        rateLimitSetSearch = Mock RateLimit
        userSet = Mock UserSet
        repositoriesList = Mock List
        followersList = Mock PaginationAwareArrayList
        followingList = Mock PaginationAwareArrayList

        githubClient = new GithubClient(
            githubJcabiClientMock,
            githubKohsukeClientMock,
            githubEgitClientMock,
            rateLimitDAOImplMock
        )
    }

    def "gets core limit"() {
        when:
        RateLimit rateLimitSetCoreReturned = githubClient.getCoreLimit()

        then:
        1 * githubJcabiClientMock.getCoreLimit() >> rateLimitSetCore
        rateLimitSetCoreReturned == rateLimitSetCore
    }

    def "gets search limit"() {
        when:
        RateLimit rateLimitSetSearchReturned = githubClient.getSearchLimit()

        then:
        1 * githubJcabiClientMock.getSearchLimit() >> rateLimitSetSearch
        rateLimitSetSearchReturned == rateLimitSetSearch
    }

    def "gets user"() {
        when:
        UserSet userSetReturned = githubClient.getUser(LOGIN)

        then:
        1 * githubJcabiClientMock.getUser(LOGIN) >> userSet
        userSetReturned == userSet
    }

    def "gets repositories"() {
        when:
        List<Repository> repositoriesListReturned = githubClient.getRepositories(LOGIN)

        then:
        1 * githubEgitClientMock.getRepositories(LOGIN) >> repositoriesList
        repositoriesListReturned == repositoriesList
    }

    def "gets followers"() {
        when:
        PaginationAwareArrayList<UserSet> followersListReturned = githubClient.getFollowers(LOGIN, 1, 0)

        then:
        1 * githubEgitClientMock.getFollowers(LOGIN, 1, 0) >> followersList
        followersListReturned == followersList
    }

    def "gets following"() {
        when:
        PaginationAwareArrayList<UserSet> followingListReturned = githubClient.getFollowing(LOGIN, 1, 0)

        then:
        1 * githubEgitClientMock.getFollowing(LOGIN, 1, 0) >> followingList
        followingListReturned == followingList
    }

}
