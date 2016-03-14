package com.cezarykluczynski.carmen.dao.apiqueue

import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.exception.EmptyPendingRequestListException
import com.cezarykluczynski.carmen.fixture.org.hibernate.SessionFactoryFixtures
import com.cezarykluczynski.carmen.util.DateTimeConstants
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.BeforeMethod

import java.lang.reflect.Field

import org.testng.annotations.Test
import org.testng.Assert

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class PendingRequestDAOImplTest extends AbstractTestNGSpringContextTests {

    private PendingRequestDAOImpl apiqueuePendingRequestDAOImpl

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    @Autowired
    private UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    private GithubClient githubClient

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @BeforeMethod
    void setUp() {
        apiqueuePendingRequestDAOImpl = new PendingRequestDAOImpl(sessionFactory, githubClient)
    }

    @Test
    void findByUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity(userEntity)

        // exercise
        List<PendingRequest> pendingRequestEntitiesList = apiqueuePendingRequestDAOImpl.findByUser userEntity

        // assertion
        Assert.assertEquals pendingRequestEntitiesList.get(0).getId(), pendingRequestEntity.getId()
        Assert.assertEquals pendingRequestEntitiesList.size(), 1

        // teardown
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void create() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures.createUserFollowersEntityUsingUserEntity userEntity
        HashMap<String, Object> emptyParams = new HashMap<String, Object>()

        // exercise
        PendingRequest pendingRequestEntity = apiqueuePendingRequestDAOImpl.create(
            "RandomExecutor",
            userEntity,
            emptyParams,
            emptyParams,
            emptyParams,
            userFollowersEntity,
            0
        )

        // assertion
        Assert.assertNotNull pendingRequestEntity.getId()
        Assert.assertEquals pendingRequestEntity.getPropagationId(), userFollowersEntity.getId()

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findMostImportantPendingRequestExistingEntities() throws EmptyPendingRequestListException {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntityWith101Priority =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndPriority userEntity, 101
        PendingRequest pendingRequestEntityWith102Priority =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndPriority userEntity, 102
        PendingRequest pendingRequestEntityWith100Priority =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndPriority userEntity, 100

        // exercise
        PendingRequest pendingRequestEntityMostImportant = apiqueuePendingRequestDAOImpl.findMostImportantPendingRequest()

        // assertion
        Assert.assertEquals pendingRequestEntityMostImportant.getId(), pendingRequestEntityWith102Priority.getId()

        // teardown
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntityWith101Priority
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntityWith100Priority
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntityWith102Priority
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findMostImportantPendingRequestNoEntities() throws EmptyPendingRequestListException {
        // setup
        SessionFactory sessionFactoryMock = SessionFactoryFixtures.createSessionFactoryMockWithEmptyCriteriaList PendingRequest.class
        setSessionFactoryToDao apiqueuePendingRequestDAOImpl, sessionFactoryMock

        try {
            // exercise
            apiqueuePendingRequestDAOImpl.findMostImportantPendingRequest()

            // assertion
            Assert.assertTrue false
        } catch (Throwable e) {
            // assertion
            Assert.assertTrue e instanceof EmptyPendingRequestListException
        }

        // teardown
        setSessionFactoryToDao apiqueuePendingRequestDAOImpl, sessionFactory
    }

    @Test
    void update() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity userEntity
        pendingRequestEntity.setExecutor "ExecutorForUpdateTest"

        // exercise
        apiqueuePendingRequestDAOImpl.update pendingRequestEntity

        // assertion
        Session session = sessionFactory.openSession()
        List<PendingRequest> list = session.createQuery(
            "SELECT pr FROM api_queue.PendingRequests pr " +
            "WHERE pr.id = :id"
        )
            .setLong("id", pendingRequestEntity.getId())
            .setMaxResults(1)
            .list();
        session.close();
        Assert.assertEquals list.get(0).getExecutor(), "ExecutorForUpdateTest"

        // teardown
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void delete() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity userEntity

        Long pendingRequestEntityId = pendingRequestEntity.getId()

        // exercise
        apiqueuePendingRequestDAOImpl.delete pendingRequestEntity

        // assertion
        Session session = sessionFactory.openSession()
        List<PendingRequest> list = session.createQuery(
            "SELECT pr FROM api_queue.PendingRequests pr " +
            "WHERE pr.id = :id"
        )
            .setLong("id", pendingRequestEntityId)
            .setMaxResults(1)
            .list();
        session.close()
        Assert.assertEquals list.size(), 0

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void countByPropagationId() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures.createUserFollowersEntityUsingUserEntity userEntity
        PendingRequest pendingRequestEntity1 =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndUserFollowersEntity(
                userEntity, userFollowersEntity
            )
        PendingRequest pendingRequestEntity2 =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndUserFollowersEntity(
                userEntity, userFollowersEntity
            )

        // exercise, assertion
        Assert.assertEquals apiqueuePendingRequestDAOImpl.countByPropagationId(userFollowersEntity.getId()), 2

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity1
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity2
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void postponeRequest() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity userEntity
        pendingRequestEntity.setUpdated new Date()
        apiqueuePendingRequestDAOImpl.update pendingRequestEntity

        // exercise
        apiqueuePendingRequestDAOImpl.postponeRequest(pendingRequestEntity, DateTimeConstants.MILLISECONDS_IN_MINUTE)

        PendingRequest pendingRequestEntityFound = apiqueuePendingRequestDAOImpl.findById(pendingRequestEntity.getId())

        // assertion
        Assert.assertEquals pendingRequestEntityFound.getUpdated().getTime(), pendingRequestEntity.getUpdated().getTime()

        // teardown
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    private static void setSessionFactoryToDao(PendingRequestDAO apiqueuePendingRequestDAOImpl, SessionFactory sessionFactory) {
        Field field = apiqueuePendingRequestDAOImpl.getClass().getDeclaredField "sessionFactory"
        field.setAccessible true
        field.set apiqueuePendingRequestDAOImpl, sessionFactory
    }

}
