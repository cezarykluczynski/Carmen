package com.cezarykluczynski.carmen.executor.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList
import com.google.common.collect.Maps
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.testng.Assert

class UserGhostPaginatorExecutorTest extends IntegrationTest {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private PendingRequestDAO apiqueuePendingRequestDAOImpl

    @Autowired
    private PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    @Autowired
    private UserGhostPaginatorExecutor userGhostPaginatorExecutor

    GithubClient githubClientMock

    @Value('${executor.UserGhostPaginatorExecutor.paginationLimit}')
    private Integer limit

    private User userEntity

    private PaginationAwareArrayList<UserSet> userSetsList

    private PendingRequest pendingRequestEntity

    private HashMap<String, Object> pathParams

    private HashMap<String, Object> queryParams

    private String executor = "UsersGhostPaginator"

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    def setup() {
        githubClientMock = Mock GithubClient
        userGhostPaginatorExecutor.githubClient = githubClientMock

        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor executor
        pendingRequestEntity.setUser userEntity
        pendingRequestEntity.setPriority 0

        pathParams = Maps.newHashMap()
        pathParams.put("login", userEntity.getLogin())

        queryParams = Maps.newHashMap()
    }

    void cleanup() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "executes followers url endpoint with no follower"() {
        given:
        pathParams.put("endpoint", "followers_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        apiqueuePendingRequestDAOImpl.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = githubUserDAOImplFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(0, 0, true)

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin

        then:
        1 * githubClientMock.getFollowers(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.empty
        apiqueuePendingRequestDAOImpl.findById(pendingRequestEntity.getId()) == null
    }

    def "executes following url endpoint with no follower"() {
        given:
        pathParams.put("endpoint", "following_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        apiqueuePendingRequestDAOImpl.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = githubUserDAOImplFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(0, 0, true)

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin

        then:
        1 *  githubClientMock.getFollowing(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.empty
        apiqueuePendingRequestDAOImpl.findById(pendingRequestEntity.getId()) == null
    }

    def "execute followers url endpoint with single follower"() {
        given:
        pathParams.put("endpoint", "followers_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        apiqueuePendingRequestDAOImpl.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = githubUserDAOImplFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(1, 0, true)
        UserSet userSetFollower = UserSet.builder().login(userSetLogin).build()
        userSetsList.add userSetFollower

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin

        then:
        1 * githubClientMock.getFollowers(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.size() == 1
        apiqueuePendingRequestDAOImpl.findById(pendingRequestEntity.getId()) == null

        cleanup:
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestList.get(0)
    }

    def "executes following url endpoint with single follower"() {
        given:
        pathParams.put("endpoint", "following_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        apiqueuePendingRequestDAOImpl.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = githubUserDAOImplFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(1, 0, true)
        UserSet userSetFollower = UserSet.builder().login(userSetLogin).build()
        userSetsList.add userSetFollower

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin

        then:
        1 * githubClientMock.getFollowing(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.size() == 1
        apiqueuePendingRequestDAOImpl.findById(pendingRequestEntity.getId()) == null

        cleanup:
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestList.get(0)
    }

    def "pagination is moved"() {
        given:
        pathParams.put("endpoint", "followers_url")
        pendingRequestEntity.setPathParams pathParams
        queryParams.put("page", 1)
        pendingRequestEntity.setQueryParams queryParams
        apiqueuePendingRequestDAOImpl.create pendingRequestEntity

        String userEntityLogin = userEntity.getLogin()
        String userSetLogin = githubUserDAOImplFixtures.generateRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(1, 0, false)
        UserSet userSetFollower = UserSet.builder().login(userSetLogin).build()
        userSetsList.setNextPage 2
        userSetsList.add userSetFollower

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin
        PendingRequest pendingRequestEntityFound = apiqueuePendingRequestDAOImpl.findById(pendingRequestEntity.getId())

        then:
        1 * githubClientMock.getFollowers(userEntityLogin, limit, 1) >> userSetsList
        pendingRequestList.size() == 1
        pendingRequestEntityFound != null
        pendingRequestEntityFound.getQueryParams().get("page") == 2

        cleanup:
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestList.get(0)
    }

    def "pending request can be blocked"() {
        given:
        User userEntityBlocking = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Session session = sessionFactory.openSession()
        session.createSQLQuery('''\
            INSERT INTO github.user_followers (followee_id, follower_id) VALUES (:userEntityId, :userEntityBlockingId)
        ''')
        .setParameter("userEntityId", userEntity.getId())
        .setParameter("userEntityBlockingId", userEntityBlocking.getId())
        .executeUpdate()
        session.close()

        PendingRequest pendingRequestEntityBlocking =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity userEntityBlocking
        pendingRequestEntityBlocking.setExecutor executor
        apiqueuePendingRequestDAOImpl.update pendingRequestEntityBlocking

        queryParams.put "page", 1
        pendingRequestEntity.setQueryParams queryParams
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setUpdated new Date()
        apiqueuePendingRequestDAOImpl.create pendingRequestEntity
        Date updatedBefore = pendingRequestEntity.getUpdated()

        when:
        userGhostPaginatorExecutor.execute pendingRequestEntity
        PendingRequest pendingRequestEntityFound = apiqueuePendingRequestDAOImpl.findById pendingRequestEntity.getId()

        then:
        Assert.assertTrue updatedBefore.getTime() < pendingRequestEntityFound.getUpdated().getTime()

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntityBlocking
    }

    private List<PendingRequest> getUserGhostPendingRequestMachingLogin(login) {
        Session session = sessionFactory.openSession()

        List<PendingRequest> list = session.createQuery('''\
            SELECT pr FROM api_queue.PendingRequests pr
            WHERE pr.executor = :executor
                AND pr.pathParams like :pathParams
        ''')
            .setString("executor", "UserGhost")
            .setString("pathParams", "%${login}%")
            .setMaxResults(1)
            .list();
        session.close()

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
