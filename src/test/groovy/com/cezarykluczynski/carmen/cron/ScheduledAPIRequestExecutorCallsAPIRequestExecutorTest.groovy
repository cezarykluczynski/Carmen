package com.cezarykluczynski.carmen.cron

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import static org.mockito.Mockito.mock
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
    "classpath:spring/cron-config.xml",
    "classpath:spring/cron/ScheduledAPIRequestExecutor-mocks.xml"
])
class ScheduledAPIRequestExecutorCallsAPIRequestExecutorTest extends AbstractTestNGSpringContextTests {

    @Mock
    APIRequestExecutor apiRequestExecutor

    @Autowired
    @InjectMocks
    ScheduledAPIRequestExecutor scheduledAPIRequestExecutor

    def noTasks

    @BeforeMethod
    void setUp() {
        noTasks = System.getProperty "noScheduledTasks"
        System.clearProperty "noScheduledTasks"
        apiRequestExecutor = mock(APIRequestExecutor.class)
        when(apiRequestExecutor.run()).thenThrow(RuntimeException)
        MockitoAnnotations.initMocks this
    }

    @Test
    void testScheduledAPIRequestExecutorCallsAPIRequestExecutor() {
        scheduledAPIRequestExecutor.executePropagation()
        verify(apiRequestExecutor).run()
    }

    @AfterMethod
    void tearDown() {
        System.setProperty "noScheduledTasks", noTasks
        Mockito.reset apiRequestExecutor
    }

}
