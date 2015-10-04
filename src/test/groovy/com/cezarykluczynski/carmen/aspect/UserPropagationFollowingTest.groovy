package com.cezarykluczynski.carmen.aspect

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserPropagationFollowingTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    UserDAOImpl githubUserDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowingDAOImpl userFollowingDAOImpl

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    PendingRequestDAOImpl pendingRequestDAOImpl

    private User userEntity

    private UserFollowing userFollowing

    private List<PendingRequest> pendingRequestsList

    private String login

    @BeforeMethod
    void setUp() {
        login = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet mockedUserSet = mock UserSet.class
        when mockedUserSet.getLogin() thenReturn login
        when mockedUserSet.getRequested() thenReturn true
        userEntity = githubUserDAOImpl.create mockedUserSet
        userEntity.setFound true

        Session session = sessionFactory.openSession()
        session.update userEntity
        session.flush()
        session.close()
    }

    @Test
    void followingPropagateAfterUserCreation() {
        // exercise
        userEntity = githubUserDAOImpl.createOrUpdateRequestedEntity login
        List<UserFollowing> userFollowingList = userFollowingDAOImpl.findByUser userEntity
        userFollowing = userFollowingList.get 0
        pendingRequestsList = pendingRequestDAOImpl.findByUser userEntity

        // assertion
        assertThat(
            userFollowing,
            hasProperty("phase", is("discover"))
        )

        assertThat pendingRequestsList.size(), equalTo(2)
        assertThat pendingRequestsList.get(0).getExecutor(), equalTo("UsersGhostPaginator")
        assertThat pendingRequestsList.get(0).getPropagationId(), equalTo(userFollowing.getId())
    }

    @AfterMethod
    void tearDown() {
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntityByUserEntity userEntity
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntityByUserEntity userEntity

        Session session = sessionFactory.openSession()
        for (pendingRequest in pendingRequestsList) {
            session.delete pendingRequest
        }
        session.delete userEntity
        session.flush()
        session.close()
    }
}
