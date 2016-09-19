package com.cezarykluczynski.carmen.client.github

import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository.RateLimitRepository
import com.cezarykluczynski.carmen.set.github.Repository
import com.cezarykluczynski.carmen.set.github.UserDTO as UserSet
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList
import spock.lang.Specification

class GithubClientTest extends Specification {

    private static final String LOGIN = "LOGIN"

    private GithubClient githubClient

    private GithubJcabiClient githubJcabiClientMock

    private GithubEgitClient githubEgitClientMock

    private GithubKohsukeClient githubKohsukeClientMock

    private RateLimitRepository rateLimitRepositoryMock

    private RateLimitDTO rateLimitSetCore

    private RateLimitDTO rateLimitSetSearch

    private UserSet userSet

    private PaginationAwareArrayList followersList

    private PaginationAwareArrayList followingList

    private List<Repository> repositoriesList

    def setup() {
        githubJcabiClientMock = Mock GithubJcabiClient
        githubEgitClientMock = Mock GithubEgitClient
        githubKohsukeClientMock = Mock GithubKohsukeClient
        rateLimitRepositoryMock = Mock RateLimitRepository
        rateLimitSetCore = Mock RateLimitDTO
        rateLimitSetSearch = Mock RateLimitDTO
        userSet = Mock UserSet
        repositoriesList = Mock List
        followersList = Mock PaginationAwareArrayList
        followingList = Mock PaginationAwareArrayList

        githubClient = new GithubClient(
            githubJcabiClientMock,
            githubKohsukeClientMock,
            githubEgitClientMock,
            rateLimitRepositoryMock
        )
    }

    def "gets core limit"() {
        when:
        RateLimitDTO rateLimitSetCoreReturned = githubClient.getCoreLimit()

        then:
        1 * githubJcabiClientMock.getCoreLimit() >> rateLimitSetCore
        rateLimitSetCoreReturned == rateLimitSetCore
    }

    def "gets search limit"() {
        when:
        RateLimitDTO rateLimitSetSearchReturned = githubClient.getSearchLimit()

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
