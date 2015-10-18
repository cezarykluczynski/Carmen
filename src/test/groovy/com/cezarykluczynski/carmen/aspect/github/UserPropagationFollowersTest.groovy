package com.cezarykluczynski.carmen.aspect.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserFollowersPropagation

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
class UserPropagationFollowersTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImpl githubUserDAOImpl

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
