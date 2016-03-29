package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github

import com.cezarykluczynski.carmen.cron.DatabaseManageableTask
import com.cezarykluczynski.carmen.dao.github.UserDAO
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.JerseyTestNg.ContainerPerClassTest
import org.glassfish.jersey.test.TestProperties
import org.json.JSONObject
import org.junit.Test
import org.testng.Assert

import javax.ws.rs.core.Application
import javax.ws.rs.core.Response

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class UsersImportCronImplTest extends ContainerPerClassTest {

    private static final Integer highestGitHubUserId = 150

    private UserDAO userDAO

    private DatabaseManageableTask usersImportTask

    @Override
    Object getProperty(String property) {
        return super.getProperty(property)
    }

    @Override
    protected Application configure() {
        enable TestProperties.LOG_TRAFFIC
        enable TestProperties.CONTAINER_FACTORY
        enable TestProperties.DUMP_ENTITY

        userDAO = mock UserDAO.class
        usersImportTask = mock DatabaseManageableTask.class
        UsersImportCronImpl usersImportCron = new UsersImportCronImpl(userDAO, usersImportTask)
        return new ResourceConfig().registerInstances(usersImportCron)
    }

    @Test
    void "import status contains highest github id"() {
        when userDAO.findHighestGitHubUserId() thenReturn highestGitHubUserId

        Response response = target().path("/admin/github/cron/users_import").request().get()
        BufferedInputStream entity = (BufferedInputStream) response.getEntity()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(entity.getText())

        Assert.assertEquals responseStatus, 200
        Assert.assertEquals responseBody.getInt("highestGitHubUserId"), highestGitHubUserId
    }

}
