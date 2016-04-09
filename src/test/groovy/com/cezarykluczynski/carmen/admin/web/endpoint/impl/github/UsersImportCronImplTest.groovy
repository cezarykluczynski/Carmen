package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github

import com.cezarykluczynski.carmen.cron.DatabaseManageableTask
import com.cezarykluczynski.carmen.dao.github.UserDAO
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.JerseyTestNg.ContainerPerClassTest
import org.glassfish.jersey.test.TestProperties
import org.json.JSONObject
import org.junit.Test
import org.testng.Assert

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Application
import javax.ws.rs.core.Form
import javax.ws.rs.core.Response

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
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

    @Test
    void "task can be enabled"() {
        when(usersImportTask.isEnabled()) thenReturn false
        doNothing().when(usersImportTask).enable()
        doNothing().when(usersImportTask).disable()

        Entity<Form> formEntity = Entity.form(new Form("enabled", "true"))
        Response response = target().path("/admin/github/cron/users_import").request().post(formEntity)
        BufferedInputStream entity = (BufferedInputStream) response.getEntity()
        JSONObject responseBody = new JSONObject(entity.getText())

        verify(usersImportTask).enable()
        verify(usersImportTask, never()).disable()
        Assert.assertEquals response.getStatus(), 200
        Assert.assertEquals responseBody.getBoolean("enabled"), true
    }

    @Test
    void "task can be disabled"() {
        when(usersImportTask.isEnabled()) thenReturn true
        doNothing().when(usersImportTask).disable()
        doNothing().when(usersImportTask).enable()

        Entity<Form> formEntity = Entity.form(new Form("enabled", "false"))
        Response response = target().path("/admin/github/cron/users_import").request().post(formEntity)
        BufferedInputStream entity = (BufferedInputStream) response.getEntity()
        JSONObject responseBody = new JSONObject(entity.getText())

        verify(usersImportTask).disable()
        verify(usersImportTask, never()).enable()
        Assert.assertEquals response.getStatus(), 200
        Assert.assertEquals responseBody.getBoolean("enabled"), false
    }

}
