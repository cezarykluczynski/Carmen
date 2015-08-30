package com.cezarykluczynski.carmen.cron.github.executor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.propagation.UserFollowersFollowingReportToSleepPhasePropagation
import com.cezarykluczynski.carmen.model.github.User

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.doThrow
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test


@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/cron/UserFollowersFollowingReportToSleepPhasePropagationExecutor-mocks.xml"
])
class UserFollowersFollowingReportToSleepPhasePropagationExecutorTest extends AbstractTestNGSpringContextTests {

    @Mock
    UserDAOImpl githubUserDAOImpl

    @Mock
    UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase

    @Autowired
    @InjectMocks
    UserFollowersFollowingReportToSleepPhasePropagationExecutor userFollowersFollowingReportToSleepPhasePropagationExecutor

    @BeforeMethod
    void setUp() {
        User userEntity = new User()
        githubUserDAOImpl = mock UserDAOImpl.class
        propagationUserFollowersFollowingReportToSleepPhase = mock UserFollowersFollowingReportToSleepPhasePropagation.class
        MockitoAnnotations.initMocks this
        when githubUserDAOImpl.findUserInReportFollowersFolloweesPhase() thenReturn userEntity
        when propagationUserFollowersFollowingReportToSleepPhase.propagate() thenThrow RuntimeException
    }

    @Test
    void testUserFollowersFollowingReportToSleepPhasePropagationExecutor() {
        userFollowersFollowingReportToSleepPhasePropagationExecutor.run()
        verify(propagationUserFollowersFollowingReportToSleepPhase).propagate()
    }

    @AfterMethod
    void tearDown() {
        Mockito.reset githubUserDAOImpl
        Mockito.reset propagationUserFollowersFollowingReportToSleepPhase
    }

}
