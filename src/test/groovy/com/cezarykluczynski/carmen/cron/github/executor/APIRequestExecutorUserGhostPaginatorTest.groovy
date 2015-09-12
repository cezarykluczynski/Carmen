package com.cezarykluczynski.carmen.cron.github.executor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.executor.github.UserGhostPaginatorExecutor

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
    "classpath:spring/cron-config.xml"
])
class APIRequestExecutorUserGhostPaginatorTest extends AbstractTestNGSpringContextTests {

    @Mock
    PendingRequestDAOImpl apiqueuePendingRequestDao

    @Mock
    UserGhostPaginatorExecutor userGhostPaginatorExecutor

    @Autowired
    @InjectMocks
    APIRequestExecutor apiRequestExecutor

    PendingRequest pendingRequestEntity

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.initMocks this
        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "UsersGhostPaginator"
        when apiqueuePendingRequestDao.findMostImportantPendingRequest() thenReturn pendingRequestEntity
        when(userGhostPaginatorExecutor.execute()).thenThrow(RuntimeException)
    }

    @Test
    void apiRequestExecutorRunsUserGhostPaginatorExecutor() {
        apiRequestExecutor.run()
        verify(userGhostPaginatorExecutor, times(1)).execute(pendingRequestEntity)
    }

    @AfterMethod
    void tearDown() {
        Mockito.reset apiqueuePendingRequestDao
        Mockito.reset userGhostPaginatorExecutor
    }

}
