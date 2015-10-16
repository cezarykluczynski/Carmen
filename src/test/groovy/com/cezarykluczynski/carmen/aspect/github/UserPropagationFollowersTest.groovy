package com.cezarykluczynski.carmen.aspect.github

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
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserPropagationFollowersTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    UserDAOImpl githubUserDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowersDAOImpl userFollowersDAOImpl

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    PendingRequestDAOImpl pendingRequestDAOImpl

    private User userEntity

    private UserFollowers userFollowers

    private List<PendingRequest> pendingRequestsList

    private String login

    //@Test
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
    void followersPropagateAfterUserCreation() {
        // exercise
        userEntity = githubUserDAOImpl.createOrUpdateRequestedEntity login
        List<UserFollowers> userFollowersList = userFollowersDAOImpl.findByUser userEntity
        userFollowers = userFollowersList.get 0
        pendingRequestsList = pendingRequestDAOImpl.findByUser userEntity

        // assertion
        assertThat(
            userFollowers,
            hasProperty("phase", is("discover"))
        )

        assertThat pendingRequestsList.size(), equalTo(2)
        assertThat pendingRequestsList.get(1).getExecutor(), equalTo("UsersGhostPaginator")
        assertThat pendingRequestsList.get(1).getPropagationId(), equalTo(userFollowers.getId())
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
