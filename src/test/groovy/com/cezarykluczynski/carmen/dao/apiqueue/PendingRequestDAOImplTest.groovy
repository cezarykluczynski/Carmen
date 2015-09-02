package com.cezarykluczynski.carmen.dao.apiqueue

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.propagations.UserFollowers

import static org.mockito.Mockito.when
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
    PendingRequestDAOImpl apiqueuePendingRequestDao

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Test
    void testFindByUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntity(userEntity)

        List<PendingRequest> pendingRequestEntitiesList = apiqueuePendingRequestDao.findByUser userEntity
        Assert.assertEquals pendingRequestEntitiesList.get(0).getId(), pendingRequestEntity.getId()
        Assert.assertEquals pendingRequestEntitiesList.size(), 1

        // teardown
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void testCreate() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures.createUserFollowersEntityUsingUserEntity userEntity

        HashMap<String, Object> emptyParams = new HashMap<String, Object>()
        PendingRequest pendingRequestEntity = apiqueuePendingRequestDao.create(
            "RandomExecutor",
            userEntity,
            emptyParams,
            emptyParams,
            emptyParams,
            userFollowersEntity,
            0
        )

        Assert.assertNotNull pendingRequestEntity.getId()
        Assert.assertEquals pendingRequestEntity.getPropagationId(), userFollowersEntity.getId()

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
