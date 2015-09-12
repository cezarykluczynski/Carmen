package com.cezarykluczynski.carmen.executor.github

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Criteria

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.beans.factory.annotation.Value

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList
import com.cezarykluczynski.carmen.provider.github.GithubProvider

import static org.mockito.Mockito.when
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserGhostPaginatorExecutorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao

    @Autowired
    PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    @Autowired
    @InjectMocks
    UserGhostPaginatorExecutor userGhostPaginatorExecutor

    @Mock
    GithubProvider githubProvider

    @Value('${executor.UserGhostPaginatorExecutor.paginationLimit}')
    private Integer limit

    User userEntity

    PaginationAwareArrayList<User> userSetsList

    PendingRequest pendingRequestEntity

    HashMap<String, Object> pathParams

    HashMap<String, Object> queryParams

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @BeforeMethod
    void setUp() {
        githubProvider = mock GithubProvider.class
        MockitoAnnotations.initMocks this

        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "UsersGhostPaginator"
        pendingRequestEntity.setUser userEntity
        pendingRequestEntity.setPriority 0

        pathParams = new HashMap<String, Object>()
        pathParams.put("login", userEntity.getLogin())

        queryParams = new HashMap<String, Object>()
    }

    @Test
    void executeFollowersUrlEndpointWithNoFollower() {
        // setup
        pathParams.put("endpoint", "followers_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        apiqueuePendingRequestDao.create pendingRequestEntity

        String userEntitylogin = userEntity.getLogin()
        String userSetLogin = githubUserDAOImplFixtures.getRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(0, 0, true)
        when githubProvider.getFollowers(userEntitylogin, limit, 1) thenReturn userSetsList

        // excercise
        userGhostPaginatorExecutor.execute pendingRequestEntity

        // assertion
        verify(githubProvider).getFollowers(userEntitylogin, limit, 1)

        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin
        Assert.assertEquals pendingRequestList.size(), 0
    }

    @Test
    void executeFollowingUrlEndpointWithNoFollower() {
        // setup
        pathParams.put("endpoint", "following_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        apiqueuePendingRequestDao.create pendingRequestEntity

        String userEntitylogin = userEntity.getLogin()
        String userSetLogin = githubUserDAOImplFixtures.getRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(0, 0, true)
        when githubProvider.getFollowing(userEntitylogin, limit, 1) thenReturn userSetsList

        // excercise
        userGhostPaginatorExecutor.execute pendingRequestEntity

        // assertion
        verify(githubProvider).getFollowing(userEntitylogin, limit, 1)

        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin
        Assert.assertEquals pendingRequestList.size(), 0
    }

    @Test
    void executeFollowersUrlEndpointWithSingleFollower() {
        // setup
        pathParams.put("endpoint", "followers_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        apiqueuePendingRequestDao.create pendingRequestEntity

        String userEntitylogin = userEntity.getLogin()
        String userSetLogin = githubUserDAOImplFixtures.getRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(1, 0, true)
        UserSet userSetFollower = new UserSet(null, userSetLogin)
        userSetsList.add userSetFollower
        when githubProvider.getFollowers(userEntitylogin, limit, 1) thenReturn userSetsList

        // excercise
        userGhostPaginatorExecutor.execute pendingRequestEntity

        // assertion
        verify(githubProvider).getFollowers(userEntitylogin, limit, 1)

        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin
        Assert.assertEquals pendingRequestList.size(), 1

        // teardown
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestList.get(0)
    }

    @Test
    void executeFollowingUrlEndpointWithSingleFollower() {
        // setup
        pathParams.put("endpoint", "following_url")
        pendingRequestEntity.setPathParams pathParams
        pendingRequestEntity.setQueryParams queryParams
        apiqueuePendingRequestDao.create pendingRequestEntity

        String userEntitylogin = userEntity.getLogin()
        String userSetLogin = githubUserDAOImplFixtures.getRandomLogin()

        userSetsList = createPaginationAwareArrayListWithUserSets(1, 0, true)
        UserSet userSetFollower = new UserSet(null, userSetLogin)
        userSetsList.add userSetFollower
        when githubProvider.getFollowing(userEntitylogin, limit, 1) thenReturn userSetsList

        // excercise
        userGhostPaginatorExecutor.execute pendingRequestEntity

        // assertion
        verify(githubProvider).getFollowing(userEntitylogin, limit, 1)

        List<PendingRequest> pendingRequestList = getUserGhostPendingRequestMachingLogin userSetLogin
        Assert.assertEquals pendingRequestList.size(), 1

        // teardown
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestList.get(0)
    }

    @AfterMethod
    void tearDown() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
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

    private PaginationAwareArrayList<UserSet> createPaginationAwareArrayListWithUserSets(limit, offset, lastPage) {
        userSetsList = new PaginationAwareArrayList<UserSet>()
        userSetsList.setLimit limit
        userSetsList.setOffset offset
        userSetsList.setLastPage lastPage
        return userSetsList
    }

}
