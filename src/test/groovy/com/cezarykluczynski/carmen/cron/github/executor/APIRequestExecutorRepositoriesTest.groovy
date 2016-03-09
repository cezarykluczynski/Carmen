package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO
import com.cezarykluczynski.carmen.executor.github.RepositoriesExecutor
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class APIRequestExecutorRepositoriesTest extends AbstractTestNGSpringContextTests {

    @Mock
    PendingRequestDAO apiqueuePendingRequestDAOImpl

    @Mock
    RepositoriesExecutor repositoriesExecutor

    @Autowired
    @InjectMocks
    APIRequestExecutor apiRequestExecutor

    PendingRequest pendingRequestEntity

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.initMocks this
        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "Repositories"
        when apiqueuePendingRequestDAOImpl.findMostImportantPendingRequest() thenReturn pendingRequestEntity
        when(repositoriesExecutor.execute()).thenThrow RuntimeException
    }

    @Test
    void apiRequestExecutorRunsUserGhostExecutor() {
        // exercise
        apiRequestExecutor.run()

        // assertion
        verify(repositoriesExecutor).execute(pendingRequestEntity)
    }

}
