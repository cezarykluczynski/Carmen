package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO
import com.cezarykluczynski.carmen.executor.github.UserGhostExecutor
import org.springframework.test.context.web.WebAppConfiguration

import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class APIRequestExecutorUserGhostTest extends AbstractTestNGSpringContextTests {

    @Mock
    PendingRequestDAO apiqueuePendingRequestDAOImpl

    @Mock
    UserGhostExecutor userGhostExecutor

    @Autowired
    @InjectMocks
    APIRequestExecutor apiRequestExecutor

    PendingRequest pendingRequestEntity

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.initMocks this
        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "UserGhost"
        when apiqueuePendingRequestDAOImpl.findMostImportantPendingRequest() thenReturn pendingRequestEntity
        when(userGhostExecutor.execute()).thenThrow RuntimeException
    }

    @Test
    void apiRequestExecutorRunsUserGhostExecutor() {
        // exercise
        apiRequestExecutor.run()

        // assertion
        verify(userGhostExecutor).execute(pendingRequestEntity)
    }

}
