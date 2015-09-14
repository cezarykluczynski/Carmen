package com.cezarykluczynski.carmen.cron.github.executor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl
import com.cezarykluczynski.carmen.propagation.UserFollowersPropagation

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.never
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
class UserFollowersDiscoverToReportPhasePropagationExecutorTest extends AbstractTestNGSpringContextTests {

    @Mock
    UserFollowersDAOImpl propagationsUserFollowersDao

    @Mock
    UserFollowersPropagation userFollowersPropagation

    UserFollowers userFollowersEntity

    @Autowired
    @InjectMocks
    UserFollowersDiscoverToReportPhasePropagationExecutor userFollowersDiscoverToReportPhasePropagationExecutor

    // No special meaning, just a number to be shared between mocks
    Long userFollowersEntityId = 1

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.initMocks this
        userFollowersEntity = mock UserFollowers.class
        when userFollowersEntity.getId() thenReturn userFollowersEntityId
    }

    @Test
    void userFollowersDiscoverToReportPhasePropagationExecutorExistingEntity() {
        // setup
        doNothing().when(userFollowersPropagation).tryToMoveToReportPhase(userFollowersEntityId)
        when propagationsUserFollowersDao.findOldestPropagationInDiscoverPhase() thenReturn userFollowersEntity

        // exercise
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        // assertion
        verify userFollowersPropagation tryToMoveToReportPhase(userFollowersEntityId)
    }

    @Test
    void userFollowersDiscoverToReportPhasePropagationExecutorNonExistingEntity() {
        // setup
        doNothing().when(userFollowersPropagation).tryToMoveToReportPhase userFollowersEntityId
        when propagationsUserFollowersDao.findOldestPropagationInDiscoverPhase() thenReturn null

        // exercise
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        // assertion
        verify(userFollowersPropagation, never()).tryToMoveToReportPhase userFollowersEntityId
    }

    @AfterMethod
    void tearDown() {
        Mockito.reset propagationsUserFollowersDao
    }

}
