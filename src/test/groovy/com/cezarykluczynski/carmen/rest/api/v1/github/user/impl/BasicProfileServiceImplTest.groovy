package com.cezarykluczynski.carmen.rest.api.v1.github.user.impl

import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.SchemaUpdateJob
import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.cron.DatabaseManageableTask
import com.cezarykluczynski.carmen.cron.languages.executor.SchemaUpdateExecutor
import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.rest.api.v1.github.user.api.BasicProfileService
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

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
public class BasicProfileServiceImplTest extends AbstractTestNGSpringContextTests {

    private static final String avatarUrl = "avatar_url"
    private static final String bio = "Bio"
    private static final String blog = "http://blog.com/"
    private static final String company = "Company"
    private static final String email = "email@example.com"
    private static final boolean hireable = true
    private static final String location = "Place, Country"
    private static final String login = "username"
    private static final String name = "John Doe"

    @Autowired
    @InjectMocks
    private BasicProfileService basicProfileService

    @Mock
    private UserDAO userDAO

    @BeforeMethod
    void setup() {
        userDAO = mock UserDAO.class
        MockitoAnnotations.initMocks this
    }

    @Test
    public void "existing user is shown"() {
        when userDAO.findByLogin("login") thenReturn createUserEntity()

        Response response = basicProfileService.get("login")
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        Assert.assertEquals responseStatus, 200
        Assert.assertEquals responseBody.getString("avatarUrl"), avatarUrl
        Assert.assertEquals responseBody.getString("bio"), bio
        Assert.assertEquals responseBody.getString("blog"), blog
        Assert.assertEquals responseBody.getString("company"), company
        Assert.assertEquals responseBody.getBoolean("hireable"), hireable
        Assert.assertEquals responseBody.getString("email"), email
        Assert.assertEquals responseBody.getBoolean("hireable"), hireable
        Assert.assertEquals responseBody.getString("location"), location
        Assert.assertEquals responseBody.getString("login"), login
        Assert.assertEquals responseBody.getString("name"), name
    }

    @Test
    public void "non existing user is not shown"() {
        Response response = basicProfileService.get("notALogin")
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        Assert.assertEquals responseStatus, 404
        Assert.assertEquals responseBody.getString("message"), "404 Not Found"
    }

    private static User createUserEntity() {
        User userEntity = new User()

        userEntity.setAvatarUrl avatarUrl
        userEntity.setBio bio
        userEntity.setBlog blog
        userEntity.setCompany company
        userEntity.setHireable hireable
        userEntity.setEmail email
        userEntity.setHireable hireable
        userEntity.setLocation location
        userEntity.setLogin login
        userEntity.setName name

        return userEntity
    }

}
