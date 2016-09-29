package com.cezarykluczynski.carmen.client.github

import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager
import com.cezarykluczynski.carmen.common.util.pagination.dto.Slice
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository.RateLimitRepository
import com.cezarykluczynski.carmen.set.github.RepositoryDTO
import com.cezarykluczynski.carmen.set.github.UserDTO
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

    private UserDTO userSet

    private Slice followersSlice

    private Slice followingSlice

    private List<RepositoryDTO> repositoriesList

    def setup() {
        githubJcabiClientMock = Mock GithubJcabiClient
        githubEgitClientMock = Mock GithubEgitClient
        githubKohsukeClientMock = Mock GithubKohsukeClient
        rateLimitRepositoryMock = Mock RateLimitRepository
        rateLimitSetCore = Mock RateLimitDTO
        rateLimitSetSearch = Mock RateLimitDTO
        userSet = Mock UserDTO
        repositoriesList = Mock List
        followersSlice = Mock Slice
        followingSlice = Mock Slice

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
        UserDTO userSetReturned = githubClient.getUser(LOGIN)

        then:
        1 * githubJcabiClientMock.getUser(LOGIN) >> userSet
        userSetReturned == userSet
    }

    def "gets repositories"() {
        when:
        List<RepositoryDTO> repositoriesListReturned = githubClient.getRepositories(LOGIN)

        then:
        1 * githubEgitClientMock.getRepositories(LOGIN) >> repositoriesList
        repositoriesListReturned == repositoriesList
    }

    def "gets followers"() {
        given:
        Pager pager = Mock(Pager)

        when:
        Slice<UserDTO> followersListReturned = githubClient.getFollowers(LOGIN, pager)

        then:
        1 * githubEgitClientMock.getFollowers(LOGIN, pager) >> followersSlice
        followersListReturned == followersSlice
    }

    def "gets following"() {
        given:
        Pager pager = Mock(Pager)

        when:
        Slice<UserDTO> followingListReturned = githubClient.getFollowing(LOGIN, pager)

        then:
        1 * githubEgitClientMock.getFollowing(LOGIN, pager) >> followingSlice
        followingListReturned == followingSlice
    }

}
