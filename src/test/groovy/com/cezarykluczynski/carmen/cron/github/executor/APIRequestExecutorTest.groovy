package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO
import com.cezarykluczynski.carmen.executor.github.UserGhostExecutor
import com.cezarykluczynski.carmen.executor.github.UserGhostPaginatorExecutor
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.*

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class APIRequestExecutorTest extends AbstractTestNGSpringContextTests {

    @Mock
    PendingRequestDAO apiqueuePendingRequestDAOImpl

    @Mock
    UserGhostExecutor userGhostExecutor

    @Mock
    UserGhostPaginatorExecutor userGhostPaginatorExecutor

    @Autowired
    @InjectMocks
    APIRequestExecutor apiRequestExecutor

    User userEntity

    @BeforeMethod
    void setUp() {
        userEntity = new User()
        MockitoAnnotations.initMocks this
    }

    @Test
    void runNonExistingExecutor() {
        // setup
        PendingRequest pendingRequestEntityMock = mock PendingRequest.class
        when apiqueuePendingRequestDAOImpl.findMostImportantPendingRequest() thenReturn pendingRequestEntityMock
        doNothing().when(userGhostExecutor).execute()
        doNothing().when(userGhostPaginatorExecutor).execute()
        when pendingRequestEntityMock.getExecutor() thenReturn "NotExistingExecutor"

        // exercise
        apiRequestExecutor.run()

        // assertion
        verify(apiqueuePendingRequestDAOImpl).findMostImportantPendingRequest()
        verify(userGhostExecutor, never()).execute()
        verify(userGhostPaginatorExecutor, never()).execute()
        verify(pendingRequestEntityMock).getExecutor()
    }

    @Test
    void runNoPendingRequests() {
        // setup
        when apiqueuePendingRequestDAOImpl.findMostImportantPendingRequest() thenReturn null

        // exercise
        apiRequestExecutor.run()

        // assertion
        verify(apiqueuePendingRequestDAOImpl).findMostImportantPendingRequest()
    }

    @AfterMethod
    void tearDown() {
        reset apiqueuePendingRequestDAOImpl
        reset userGhostExecutor
        reset userGhostPaginatorExecutor
    }

}
