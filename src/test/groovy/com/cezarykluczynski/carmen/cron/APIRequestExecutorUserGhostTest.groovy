package com.cezarykluczynski.carmen.cron

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.executor.UserGhostExecutor

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
        doThrow(RuntimeException).when(userGhostExecutor).execute()
    }

    @Test
    void testAPIRequestExecutorRunsUserGhostExecutor() {
        apiRequestExecutor.run()
        verify(userGhostExecutor, times(1)).execute(pendingRequestEntity)
    }

}
