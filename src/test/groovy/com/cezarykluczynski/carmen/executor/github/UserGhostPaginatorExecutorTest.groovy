package com.cezarykluczynski.carmen.executor.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.cezarykluczynski.carmen.set.github.UserDTO as UserSet
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList
import com.cezarykluczynski.carmen.util.db.TransactionalExecutor
import com.google.common.collect.Maps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import javax.persistence.EntityManager

class UserGhostPaginatorExecutorTest extends IntegrationTest {

    @Autowired
    private EntityManager entityManager

    @Autowired
    TransactionalExecutor transactionalExecutor

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private PendingRequestRepository pendingRequestRepository

    @Autowired
    private PendingRequestRepositoryFixtures pendingRequestRepositoryFixtures

    @Autowired
    private UserGhostPaginatorExecutor userGhostPaginatorExecutor

    private GithubClient githubClientMock

    @Value('${executor.UserGhostPaginatorExecutor.paginationLimit}')
    private Integer limit

    private User userEntity

    private PaginationAwareArrayList<UserSet> userSetsList

    private PendingRequest pendingRequestEntity

    private HashMap<String, Object> pathParams

    private HashMap<String, Object> queryParams

    private String executor = "UsersGhostPaginator"

    def setup() {
        githubClientMock = Mock GithubClient
        userGhostPaginatorExecutor.githubClient = githubClientMock

        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()

        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor executor
        pendingRequestEntity.setUser userEntity
        pendingRequestEntity.setPriority 0

        pathParams = Maps.newHashMap()
        pathParams.put("login", userEntity.getLogin())

        queryParams = Maps.newHashMap()
    }

    void cleanup() {
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "executes followers url endpoint with no follower"() {
        given:
        pathParams.put("endpoint", "followers_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        pendingRequestRepository.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = userRepositoryFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(0, 0, true)

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin

        then:
        1 * githubClientMock.getFollowers(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.empty
        pendingRequestRepository.findOne(pendingRequestEntity.getId()) == null
    }

    def "executes following url endpoint with no follower"() {
        given:
        pathParams.put("endpoint", "following_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        pendingRequestRepository.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = userRepositoryFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(0, 0, true)

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin

        then:
        1 *  githubClientMock.getFollowing(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.empty
        pendingRequestRepository.findOne(pendingRequestEntity.getId()) == null
    }

    def "execute followers url endpoint with single follower"() {
        given:
        pathParams.put("endpoint", "followers_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        pendingRequestRepository.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = userRepositoryFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(1, 0, true)
        UserSet userSetFollower = UserSet.builder().login(userSetLogin).build()
        userSetsList.add userSetFollower

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin

        then:
        1 * githubClientMock.getFollowers(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.size() == 1
        pendingRequestRepository.findOne(pendingRequestEntity.getId()) == null

        cleanup:
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestList.get(0)
    }

    def "executes following url endpoint with single follower"() {
        given:
        pathParams.put("endpoint", "following_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        pendingRequestRepository.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = userRepositoryFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(1, 0, true)
        UserSet userSetFollower = UserSet.builder().login(userSetLogin).build()
        userSetsList.add userSetFollower

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin

        then:
        1 * githubClientMock.getFollowing(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.size() == 1
        pendingRequestRepository.findOne(pendingRequestEntity.getId()) == null

        cleanup:
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestList.get(0)
    }

    def "pagination is moved"() {
        given:
        pathParams.put("endpoint", "followers_url")
        pendingRequestEntity.setPathParams pathParams
        queryParams.put("page", 1)
        pendingRequestEntity.setQueryParams queryParams
        pendingRequestRepository.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = userRepositoryFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(1, 0, false)
        UserSet userSetFollower = UserSet.builder().login(userSetLogin).build()
        userSetsList.setNextPage 2
        userSetsList.add userSetFollower

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin
        PendingRequest pendingRequestEntityFound = pendingRequestRepository.findOne(pendingRequestEntity.getId())

        then:
        1 * githubClientMock.getFollowers(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.size() == 1
        pendingRequestEntityFound != null
        pendingRequestEntityFound.getQueryParams().get("page") == 2

        cleanup:
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestList.get(0)
    }

    def "pending request can be blocked"() {
        given:
        User userEntityBlocking = userRepositoryFixtures.createFoundRequestedUserEntity()

        transactionalExecutor.execute({ entityManager ->
            entityManager.createNativeQuery('''\
                               INSERT INTO github.user_followers (followee_id, follower_id) VALUES (:userEntityId, :userEntityBlockingId)
                           ''')
                    .setParameter("userEntityId", userEntity.getId())
                    .setParameter("userEntityBlockingId", userEntityBlocking.getId())
                    .executeUpdate()
        })


        PendingRequest pendingRequestEntityBlocking =
            pendingRequestRepositoryFixtures.createPendingRequestEntityUsingUserEntity userEntityBlocking
        pendingRequestEntityBlocking.setExecutor executor
        pendingRequestRepository.save pendingRequestEntityBlocking

        queryParams.put "page", 1
        pendingRequestEntity.setQueryParams queryParams
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setUpdated new Date()
        pendingRequestRepository.create pendingRequestEntity
        Date updatedBefore = pendingRequestEntity.getUpdated()

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        PendingRequest pendingRequestEntityFound = pendingRequestRepository.findOne pendingRequestEntity.getId()

        then:
        updatedBefore.getTime() < pendingRequestEntityFound.getUpdated().getTime()

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntityBlocking
    }

    private List<PendingRequest> getUserGhostPendingRequestMachingLogin(login) {
        List<PendingRequest> list = entityManager.createQuery('''\
            SELECT pr FROM api_queue.PendingRequests pr
            WHERE pr.executor = :executor
                AND pr.pathParams like :pathParams
        ''')
            .setParameter("executor", "UserGhost")
            .setParameter("pathParams", '%' + login + '%')
            .setMaxResults(1)
            .getResultList()

        return list
    }

    private PaginationAwareArrayList<UserSet> createPaginationAwareArrayListWithUserSets(Integer limit, Integer offset,
            Boolean lastPage) {
        userSetsList = new PaginationAwareArrayList<UserSet>()
        userSetsList.setLimit limit
        userSetsList.setOffset offset
        userSetsList.setLastPage lastPage
        return userSetsList
    }

}
