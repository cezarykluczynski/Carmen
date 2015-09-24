package com.cezarykluczynski.carmen.cron.github.executor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.exception.EmptyPendingRequestListException
import com.cezarykluczynski.carmen.executor.github.UserGhostExecutor
import com.cezarykluczynski.carmen.executor.github.UserGhostPaginatorExecutor

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.never
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import java.io.IOException

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
class APIRequestExecutorTest extends AbstractTestNGSpringContextTests {

    @Mock
    PendingRequestDAOImpl apiqueuePendingRequestDao

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
        when apiqueuePendingRequestDao.findMostImportantPendingRequest() thenReturn pendingRequestEntityMock
        doNothing().when(userGhostExecutor).execute()
        doNothing().when(userGhostPaginatorExecutor).execute()
        when pendingRequestEntityMock.getExecutor() thenReturn "NotExistingExecutor"

        // exercise
        apiRequestExecutor.run()

        // assertion
        verify(apiqueuePendingRequestDao).findMostImportantPendingRequest()
        verify(userGhostExecutor, never()).execute()
        verify(userGhostPaginatorExecutor, never()).execute()
        verify(pendingRequestEntityMock).getExecutor()
    }

    @Test
    void runEmptyPendingRequestListException() {
        // setup
        when apiqueuePendingRequestDao.findMostImportantPendingRequest() thenThrow EmptyPendingRequestListException

        // exercise
        apiRequestExecutor.run()

        // assertion
        verify(apiqueuePendingRequestDao).findMostImportantPendingRequest()
    }

    @Test
    void runIOException() {
        // setup
        when apiqueuePendingRequestDao.findMostImportantPendingRequest() thenThrow IOException

        // exercise
        apiRequestExecutor.run()

        // assertion
        verify(apiqueuePendingRequestDao).findMostImportantPendingRequest()
    }

    @AfterMethod
    void tearDown() {
        Mockito.reset apiqueuePendingRequestDao
        Mockito.reset userGhostExecutor
        Mockito.reset userGhostPaginatorExecutor
    }

}