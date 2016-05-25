package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github

import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.SchemaUpdateJob
import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.UsersImportCron
import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.cron.DatabaseManageableTask
import com.cezarykluczynski.carmen.cron.languages.executor.SchemaUpdateExecutor
import com.cezarykluczynski.carmen.cron.languages.util.SchemaUpdateFilesStateHelper
import org.apache.commons.lang.math.RandomUtils
import org.json.JSONObject
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import javax.ws.rs.core.Response
import java.time.LocalDateTime

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
public class SchemaUpdateJobImplTest extends AbstractTestNGSpringContextTests {

    private static final LocalDateTime UPDATED = LocalDateTime.of(2016, 1, 1, 0, 0)
    private static final boolean ENABLED = RandomUtils.nextBoolean()
    private static final boolean RUNNING = RandomUtils.nextBoolean()
    private static final boolean HAS_FILES_CHANGED = RandomUtils.nextBoolean()

    @Autowired
    @InjectMocks
    private SchemaUpdateJob schemaUpdateJob

    @Autowired
    @InjectMocks
    private UsersImportCron usersImportCron

    @Mock
    private SchemaUpdateExecutor schemaUpdateExecutor

    @Mock
    private DatabaseManageableTask schemaUpdateTask

    @Mock
    private SchemaUpdateFilesStateHelper schemaUpdateFilesStateHelper

    @BeforeMethod
    void setup() {
        schemaUpdateExecutor = mock SchemaUpdateExecutor
        schemaUpdateTask = mock DatabaseManageableTask
        schemaUpdateFilesStateHelper = mock SchemaUpdateFilesStateHelper
        MockitoAnnotations.initMocks this
    }

    @Test
    void getStatus() {
        when schemaUpdateTask.isEnabled() thenReturn ENABLED
        when schemaUpdateTask.isRunning() thenReturn RUNNING
        when schemaUpdateTask.getUpdated() thenReturn UPDATED
        when schemaUpdateFilesStateHelper.hasFilesChanged() thenReturn HAS_FILES_CHANGED

        Response response = schemaUpdateJob.getStatus()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        Assert.assertEquals responseStatus, 200
        Assert.assertEquals(LocalDateTime.parse(responseBody.getString("updated")), UPDATED)
        Assert.assertEquals(responseBody.getBoolean("enabled"), ENABLED)
        Assert.assertEquals(responseBody.getBoolean("running"), RUNNING)
        Assert.assertEquals(responseBody.getBoolean("saved"), !HAS_FILES_CHANGED)
        Assert.assertTrue responseBody.getString("linguistVersion").matches("\\d\\.\\d\\.\\d")
    }

    @Test
    void run() {
        when schemaUpdateTask.isEnabled() thenReturn ENABLED
        when schemaUpdateTask.isRunning() thenReturn RUNNING
        when(schemaUpdateTask.getUpdated()).thenReturn UPDATED
        when schemaUpdateFilesStateHelper.hasFilesChanged() thenReturn HAS_FILES_CHANGED

        doNothing().when(schemaUpdateExecutor).run()

        Response response = schemaUpdateJob.run()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        Assert.assertEquals responseStatus, 200
        Assert.assertEquals(LocalDateTime.parse(responseBody.getString("updated")), UPDATED)
        Assert.assertEquals(responseBody.getBoolean("saved"), !HAS_FILES_CHANGED)
        Assert.assertTrue responseBody.getString("linguistVersion").matches("\\d\\.\\d\\.\\d")
        verify(schemaUpdateExecutor).run()
    }

}
