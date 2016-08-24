package com.cezarykluczynski.carmen.dao.apiqueue

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.util.DateTimeConstants
import com.google.common.collect.Maps
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired

class PendingRequestDAOImplTest extends IntegrationTest {

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

    def setup() {
        apiqueuePendingRequestDAOImpl = new PendingRequestDAOImpl(sessionFactory, githubClient)
    }

    def "finds pending requests by user"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity(userEntity)

        when:
        List<PendingRequest> pendingRequestEntitiesList = apiqueuePendingRequestDAOImpl.findByUser userEntity

        then:
        pendingRequestEntitiesList[0].id == pendingRequestEntity.id
        pendingRequestEntitiesList.size() == 1

        cleanup:
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "creates pending request"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
                .createUserFollowersEntityUsingUserEntity userEntity
        HashMap<String, Object> emptyParams = Maps.newHashMap()

        when:
        PendingRequest pendingRequestEntity = apiqueuePendingRequestDAOImpl.create(
            "RandomExecutor",
            userEntity,
            emptyParams,
            emptyParams,
            emptyParams,
            userFollowersEntity,
            0
        )

        then:
        pendingRequestEntity.id != null
        pendingRequestEntity.getPropagationId() == userFollowersEntity.getId()

        cleanup:
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "finds most important pending request based on priority"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntityWith101Priority =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndPriority userEntity, 101
        PendingRequest pendingRequestEntityWith102Priority =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndPriority userEntity, 102
        PendingRequest pendingRequestEntityWith100Priority =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndPriority userEntity, 100

        when:
        PendingRequest pendingRequestEntityMostImportant = apiqueuePendingRequestDAOImpl
                .findMostImportantPendingRequest()

        then:
        pendingRequestEntityMostImportant.id == pendingRequestEntityWith102Priority.id

        cleanup:
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntityWith101Priority
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntityWith100Priority
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntityWith102Priority
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "returns null when most important pending request cannot be found"() {
        given:
        SessionFactory sessionFactoryMock = createSessionFactoryMockWithEmptyCriteriaList PendingRequest.class
        setSessionFactoryToDao apiqueuePendingRequestDAOImpl, sessionFactoryMock

        when:
        PendingRequest pendingRequest = apiqueuePendingRequestDAOImpl.findMostImportantPendingRequest()

        then:
        pendingRequest == null

        cleanup:
        setSessionFactoryToDao apiqueuePendingRequestDAOImpl, sessionFactory
    }

    def "updates executor"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity userEntity
        pendingRequestEntity.setExecutor "ExecutorForUpdateTest"

        when:
        apiqueuePendingRequestDAOImpl.update pendingRequestEntity

        then:
        findPendingRequestById(pendingRequestEntity.getId()).getExecutor() == "ExecutorForUpdateTest"

        cleanup:
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "deletes entity"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity userEntity

        Long pendingRequestEntityId = pendingRequestEntity.getId()

        when:
        apiqueuePendingRequestDAOImpl.delete pendingRequestEntity

        then:
        findPendingRequestById(pendingRequestEntityId) == null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "counts pending request by propagation id"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
                .createUserFollowersEntityUsingUserEntity userEntity
        PendingRequest pendingRequestEntity1 = apiqueuePendingRequestDAOImplFixtures
                .createPendingRequestEntityUsingUserEntityAndUserFollowersEntity(userEntity, userFollowersEntity)
        PendingRequest pendingRequestEntity2 = apiqueuePendingRequestDAOImplFixtures
                .createPendingRequestEntityUsingUserEntityAndUserFollowersEntity(userEntity, userFollowersEntity)

        when:
        Long count = apiqueuePendingRequestDAOImpl.countByPropagationId userFollowersEntity.getId()

        then:
        count == 2L

        cleanup:
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity1
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity2
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "postpones pending request"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity userEntity
        pendingRequestEntity.setUpdated new Date()
        apiqueuePendingRequestDAOImpl.update pendingRequestEntity

        when:
        apiqueuePendingRequestDAOImpl.postponeRequest(pendingRequestEntity, DateTimeConstants.MILLISECONDS_IN_MINUTE)
        PendingRequest pendingRequestEntityFound = apiqueuePendingRequestDAOImpl.findById(pendingRequestEntity.getId())

        then:
        pendingRequestEntityFound.updated.time == pendingRequestEntity.updated.time

        cleanup:
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    private static void setSessionFactoryToDao(PendingRequestDAOImpl apiqueuePendingRequestDAOImpl,
                                               SessionFactory sessionFactory) {
        apiqueuePendingRequestDAOImpl.sessionFactory = sessionFactory
    }

    private PendingRequest findPendingRequestById(Long id) {
        Session session = sessionFactory.openSession()
        List<PendingRequest> list = session.createQuery(
                "SELECT pr FROM api_queue.PendingRequests pr " +
                        "WHERE pr.id = :id"
        )
                .setLong("id", id)
                .setMaxResults(1)
                .list()
        session.close()
        return list.empty ? null : list[0]
    }

}
