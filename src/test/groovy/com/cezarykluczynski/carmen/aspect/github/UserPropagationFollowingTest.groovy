package com.cezarykluczynski.carmen.aspect.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserFollowingPropagation

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.doNothing
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserPropagationFollowingTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAO githubUserDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Mock
    UserFollowingPropagation userFollowingPropagation

    @Autowired
    @InjectMocks
    UserPropagationFollowing userPropagationFollowing

    private User userEntity

    @BeforeMethod
    void setUp() {
        userFollowingPropagation = mock UserFollowingPropagation.class
        doNothing().when(userFollowingPropagation).propagate()
        MockitoAnnotations.initMocks this
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
    }

    @Test
    void followingPropagateAfterUserCreation() {
        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity userEntity.getLogin()

        // assertion
        verify(userFollowingPropagation).propagate()
    }

    @AfterMethod
    void tearDown() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
