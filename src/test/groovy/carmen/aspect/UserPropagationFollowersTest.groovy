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
import carmen.dao.propagations.UserFollowersDAOImpl
import carmen.model.github.User
import carmen.set.github.User as UserSet
import carmen.model.propagations.UserFollowers

@ContextConfiguration(locations = [ "classpath:spring/database-config.xml", "classpath:spring/mvc-core-config.xml" ])
class UserFollowersTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    UserDAOImpl githubUserDAOImpl

    @Autowired
    UserFollowersDAOImpl userFollowersDAOImpl

    private User userEntity

    private UserFollowers userFollowers

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
    void followersPropagateAfterUserCreation() {
        userEntity = githubUserDAOImpl.createOrUpdateRequestedEntity login
        List<UserFollowers> userFollowersList = userFollowersDAOImpl.findByUser userEntity
        userFollowers = userFollowersList.get 0
        assertThat(
            userFollowers,
            hasProperty("phase", is("discover"))
        )
    }

    @AfterMethod
    void tearDown() {
        Session session = sessionFactory.openSession()
        session.delete userEntity
        session.delete userFollowers
        session.flush()
        session.close()
    }
}
