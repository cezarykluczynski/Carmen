package com.cezarykluczynski.carmen.cron.github.executor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl
import com.cezarykluczynski.carmen.propagation.UserFollowingPropagation

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.doNothing
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
    "classpath:spring/cron-config.xml"
])
class UserFollowingDiscoverToReportPhasePropagationExecutorTest extends AbstractTestNGSpringContextTests {

    @Mock
    UserFollowingDAOImpl propagationsUserFollowingDao

    @Mock
    UserFollowingPropagation userFollowingPropagation

    UserFollowing userFollowingEntity

    @Autowired
    @InjectMocks
    UserFollowingDiscoverToReportPhasePropagationExecutor userFollowingDiscoverToReportPhasePropagationExecutor

    // No special meaning, just a number to be shared between mocks
    Long userFollowingEntityId = 1

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.initMocks this
        userFollowingEntity = mock UserFollowing.class
        when userFollowingEntity.getId() thenReturn userFollowingEntityId
        doNothing().when(userFollowingPropagation).tryToMoveToReportPhase(userFollowingEntityId)
        when propagationsUserFollowingDao.findOldestPropagationInDiscoverPhase() thenReturn userFollowingEntity
    }

    @Test
    void testUserFollowingDiscoverToReportPhasePropagationExecutor() {
        userFollowingDiscoverToReportPhasePropagationExecutor.run()
        verify userFollowingPropagation tryToMoveToReportPhase(userFollowingEntityId)
    }

}
