package com.cezarykluczynski.carmen.cron.github.executor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.propagation.UserFollowersFollowingReportToSleepPhasePropagation
import com.cezarykluczynski.carmen.model.github.User

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.never
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
    "classpath:spring/cron-config.xml"
])
class UserFollowersFollowingReportToSleepPhasePropagationExecutorTest extends AbstractTestNGSpringContextTests {

    @Mock
    UserDAOImpl githubUserDAOImpl

    @Mock
    UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase

    @Autowired
    @InjectMocks
    UserFollowersFollowingReportToSleepPhasePropagationExecutor userFollowersFollowingReportToSleepPhasePropagationExecutor

    User userEntity

    @BeforeMethod
    void setUp() {
        userEntity = new User()
        githubUserDAOImpl = mock UserDAOImpl.class
        propagationUserFollowersFollowingReportToSleepPhase = mock UserFollowersFollowingReportToSleepPhasePropagation.class
        MockitoAnnotations.initMocks this
    }

    @Test
    void userFollowersFollowingReportToSleepPhasePropagationExecutorExistingEntity() {
        // setup
        when githubUserDAOImpl.findUserInReportFollowersFolloweesPhase() thenReturn userEntity
        doNothing().when(propagationUserFollowersFollowingReportToSleepPhase).propagate()

        // exercise
        userFollowersFollowingReportToSleepPhasePropagationExecutor.run()

        // assertion
        verify(propagationUserFollowersFollowingReportToSleepPhase).propagate()
    }

    @Test
    void userFollowersFollowingReportToSleepPhasePropagationExecutorNonExistingEntity() {
        // setup
        when githubUserDAOImpl.findUserInReportFollowersFolloweesPhase() thenReturn null
        doNothing().when(propagationUserFollowersFollowingReportToSleepPhase).propagate()

        // exercise
        userFollowersFollowingReportToSleepPhasePropagationExecutor.run()

        // assertion
        verify(propagationUserFollowersFollowingReportToSleepPhase, never()).propagate()
    }

    @AfterMethod
    void tearDown() {
        Mockito.reset githubUserDAOImpl
        Mockito.reset propagationUserFollowersFollowingReportToSleepPhase
    }

}
