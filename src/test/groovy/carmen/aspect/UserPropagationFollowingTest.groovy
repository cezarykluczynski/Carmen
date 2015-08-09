package carmen.aspect

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.MatcherAssert.assertThat
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

import carmen.dao.github.UserDAOImpl
import carmen.dao.propagations.UserFollowingDAOImpl
import carmen.model.github.User
import carmen.set.github.User as UserSet
import carmen.model.propagations.UserFollowing

@ContextConfiguration(locations = [ "classpath:spring/database-config.xml", "classpath:spring/mvc-core-config.xml" ])
class UserFollowingTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    UserDAOImpl githubUserDAOImpl

    @Autowired
    UserFollowingDAOImpl userFollowingDAOImpl

    private User userEntity

    private UserFollowing userFollowing

    private String login

    @BeforeMethod
    void setUp() {
        login = "random_login" + System.currentTimeMillis()
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
        userEntity = githubUserDAOImpl.createOrUpdateRequestedEntity login
        List<UserFollowing> userFollowingList = userFollowingDAOImpl.findByUser userEntity
        userFollowing = userFollowingList.get 0
        assertThat(
            userFollowing,
            hasProperty("phase", is("discover"))
        )
    }

    @AfterMethod
    void tearDown() {
        Session session = sessionFactory.openSession()
        session.delete userEntity
        session.delete userFollowing
        session.flush()
        session.close()
    }
}
