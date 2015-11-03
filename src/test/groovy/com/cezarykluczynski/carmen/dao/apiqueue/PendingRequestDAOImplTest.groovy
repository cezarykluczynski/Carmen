package com.cezarykluczynski.carmen.dao.apiqueue

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.exception.EmptyPendingRequestListException
import com.cezarykluczynski.carmen.fixture.org.hibernate.SessionFactoryFixtures

import static org.mockito.Mockito.when
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.Test
import org.testng.Assert

import java.util.List

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class PendingRequestDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    PendingRequestDAO apiqueuePendingRequestDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    SessionFactoryFixtures sessionFactoryFixtures

    @Autowired
    private SessionFactory sessionFactory

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
        SessionFactory sessionFactoryMock = sessionFactoryFixtures.createSessionFactoryMockWithEmptyCriteriaList PendingRequest.class
        apiqueuePendingRequestDAOImpl.setSessionFactory sessionFactoryMock

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
        apiqueuePendingRequestDAOImpl.setSessionFactory sessionFactory
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

}
