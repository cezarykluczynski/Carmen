package com.cezarykluczynski.carmen.aspect.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserFollowersPropagation

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.doNothing
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.InjectMocks

@ContextConfiguration(
        classes = TestableApplicationConfiguration.class,
        loader = SpringApplicationContextLoader.class,
        locations = ["classpath:applicationContext.xml"]
)
@WebAppConfiguration
class UserPropagationFollowersTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAO githubUserDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Mock
    UserFollowersPropagation userFollowersPropagation

    @Autowired
    @InjectMocks
    UserPropagationFollowers userPropagationFollowers

    private User userEntity

    @BeforeMethod
    void setUp() {
        userFollowersPropagation = mock UserFollowersPropagation.class
        doNothing().when(userFollowersPropagation).propagate()
        MockitoAnnotations.initMocks this
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
    }

    @Test
    void followersPropagateAfterUserCreation() {
        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity userEntity.getLogin()

        // assertion
        verify(userFollowersPropagation).propagate()
    }

    @AfterMethod
    void tearDown() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
