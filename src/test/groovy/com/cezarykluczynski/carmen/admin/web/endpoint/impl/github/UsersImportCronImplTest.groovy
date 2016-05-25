package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github

import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.UsersImportCron
import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.cron.DatabaseManageableTask
import com.cezarykluczynski.carmen.dao.github.UserDAO
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

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
public class UsersImportCronImplTest extends AbstractTestNGSpringContextTests {

    private static final Integer HIGHEST_GITHUB_USER_ID = 150

    @Autowired
    @InjectMocks
    private UsersImportCron usersImportCron

    @Mock
    private UserDAO userDAO

    @Mock
    private DatabaseManageableTask usersImportTask

    @BeforeMethod
    void setup() {
        userDAO = mock UserDAO.class
        usersImportTask = mock DatabaseManageableTask.class
        MockitoAnnotations.initMocks this
    }

    @Test
    void "import status contains highest github id"() {
        when userDAO.findHighestGitHubUserId() thenReturn HIGHEST_GITHUB_USER_ID

        Response response = usersImportCron.get()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        Assert.assertEquals responseStatus, 200
        Assert.assertEquals responseBody.getInt("highestGitHubUserId"), HIGHEST_GITHUB_USER_ID
    }

    @Test
    void "task can be enabled"() {
        when(usersImportTask.isEnabled()) thenReturn false
        doNothing().when(usersImportTask).enable()
        doNothing().when(usersImportTask).disable()

        Response response = usersImportCron.updateStatus(true)
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        verify(usersImportTask).enable()
        verify(usersImportTask, never()).disable()
        Assert.assertEquals responseStatus, 200
        Assert.assertEquals responseBody.getBoolean("enabled"), true
    }

    @Test
    void "task can be disabled"() {
        when(usersImportTask.isEnabled()) thenReturn true
        doNothing().when(usersImportTask).disable()
        doNothing().when(usersImportTask).enable()

        Response response = usersImportCron.updateStatus(false)
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        verify(usersImportTask).disable()
        verify(usersImportTask, never()).enable()
        Assert.assertEquals responseStatus, 200
        Assert.assertEquals responseBody.getBoolean("enabled"), false
    }


}
