package com.cezarykluczynski.carmen.cron.github.executor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.executor.github.UserGhostExecutor

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.doThrow
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
class APIRequestExecutorUserGhostTest extends AbstractTestNGSpringContextTests {

    @Mock
    PendingRequestDAOImpl apiqueuePendingRequestDao

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
        when apiqueuePendingRequestDao.findMostImportantPendingRequest() thenReturn pendingRequestEntity
        when(userGhostExecutor.execute()).thenThrow RuntimeException
    }

    @Test
    void apiRequestExecutorRunsUserGhostExecutor() {
        // exercise
        apiRequestExecutor.run()

        // assertion
        verify(userGhostExecutor).execute(pendingRequestEntity)
    }

    @AfterMethod
    void tearDown() {
        Mockito.reset apiqueuePendingRequestDao
        Mockito.reset userGhostExecutor
    }

}
