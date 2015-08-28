package com.cezarykluczynski.carmen.cron

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.executor.UserGhostPaginatorExecutor

import static org.mockito.Mockito.when
import static org.mockito.Mockito.doThrow
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/cron-config-test.xml"
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
        doThrow(RuntimeException).when(userGhostPaginatorExecutor).execute()
    }

    @Test
    void testAPIRequestExecutorRunsUserGhostPaginatorExecutor() {
        apiRequestExecutor.run()
        verify(userGhostPaginatorExecutor, times(1)).execute(pendingRequestEntity)
    }

}
