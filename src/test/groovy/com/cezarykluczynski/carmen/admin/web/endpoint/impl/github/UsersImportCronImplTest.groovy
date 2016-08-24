package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github

import com.cezarykluczynski.carmen.cron.DatabaseManageableTask
import com.cezarykluczynski.carmen.dao.github.UserDAO
import org.json.JSONObject
import spock.lang.Specification

import javax.ws.rs.core.Response

public class UsersImportCronImplTest extends Specification {

    private static final Integer HIGHEST_GITHUB_USER_ID = 150

    private UsersImportCronImpl usersImportCron

    private UserDAO userDao

    private DatabaseManageableTask usersImportTask

    def setup() {
        userDao = Mock UserDAO
        usersImportTask = Mock DatabaseManageableTask
        usersImportCron = new UsersImportCronImpl(userDao, usersImportTask)
    }

    def "import status contains highest github id"() {
        given:
        userDao.findHighestGitHubUserId() >> HIGHEST_GITHUB_USER_ID

        when:
        Response response = usersImportCron.get()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        responseStatus == 200
        responseBody.getInt("highestGitHubUserId") == HIGHEST_GITHUB_USER_ID
    }

    def "task can be enabled"() {
        given:
        usersImportTask.isEnabled() >> false

        when:
        Response response = usersImportCron.updateStatus(true)
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        1 * usersImportTask.enable()
        0 * usersImportTask.disable()
        responseStatus == 200
        responseBody.getBoolean("enabled")
    }

    def "task can be disabled"() {
        usersImportTask.isEnabled() >> true

        when:
        Response response = usersImportCron.updateStatus(false)
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        1 * usersImportTask.disable()
        0 * usersImportTask.enable()
        responseStatus == 200
        !responseBody.getBoolean("enabled")
    }


}
