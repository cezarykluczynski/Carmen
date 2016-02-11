package com.cezarykluczynski.carmen.cron.github.executor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAO
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.propagations.Repositories
import com.cezarykluczynski.carmen.util.DateTimeConstants

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
class RepositoriesWakeUpExecutorTest extends AbstractTestNGSpringContextTests {

    @Value('${executor.RepositoriesWakeUpExecutor.refreshIntervalDays}')
    private Integer refreshIntervalDays;

    @Mock
    RepositoriesDAO propagationsRepositoriesDAOImpl

    @Mock
    PendingRequestFactory pendingRequestFactory

    @Autowired
    @InjectMocks
    RepositoriesWakeUpExecutor propagationRepositoriesWakeUpExecutor

    Repositories repositoriesEntity

    PendingRequest pendingRequestEntity

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.initMocks this

        repositoriesEntity = mock Repositories.class
        when propagationsRepositoriesDAOImpl.findOldestPropagationInSleepPhase() thenReturn repositoriesEntity
        doNothing().when(propagationsRepositoriesDAOImpl).moveToRefreshPhase(repositoriesEntity)

        pendingRequestEntity = mock PendingRequest.class
        when pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(repositoriesEntity) thenReturn pendingRequestEntity
    }

    @Test
    void testNonExistingEntity() {
        // setup
        when propagationsRepositoriesDAOImpl.findOldestPropagationInSleepPhase() thenReturn null

        // exercise
        propagationRepositoriesWakeUpExecutor.run()

        // assert it's OK: there were no NullPointerException to this point
        Assert.assertTrue true
    }

    @Test
    void testExistingEntityThatShouldNotBeUpdated() {
        // setup
        when(repositoriesEntity.getUpdated()).thenReturn(new Date())

        // exercise
        propagationRepositoriesWakeUpExecutor.run()

        // assertion
        verify(repositoriesEntity).getUpdated()
        verify(pendingRequestFactory, never()).createPendingRequestForUserRepositoriesPropagation repositoriesEntity
        verify(propagationsRepositoriesDAOImpl, never()).moveToRefreshPhase repositoriesEntity
    }

    @Test
    void testExistingEntityThatShouldBeUpdated() {
        // setup
        when(repositoriesEntity.getUpdated()).thenReturn(new Date(
            System.currentTimeMillis() - (- 1 + refreshIntervalDays * DateTimeConstants.MILLISECONDS_IN_DAY.getValue())
        ))

        // exercise
        propagationRepositoriesWakeUpExecutor.run()

        // assertion
        verify(propagationsRepositoriesDAOImpl).findOldestPropagationInSleepPhase()
        verify(pendingRequestFactory).createPendingRequestForUserRepositoriesPropagation(repositoriesEntity)
        verify(propagationsRepositoriesDAOImpl).moveToRefreshPhase(repositoriesEntity)
    }

}
