package com.cezarykluczynski.carmen.rest.api.v1.github.user.impl

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
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

    private static final String AVATAR_URL = "avatar_url"
    private static final String BIO = "Bio"
    private static final String BLOG = "http://blog.com/"
    private static final String COMPANY = "Company"
    private static final String EMAIL = "email@example.com"
    private static final boolean HIREABLE = true
    private static final String LOCATION = "Place, Country"
    private static final String LOGIN = "username"
    private static final String NAME = "John Doe"

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
        Assert.assertEquals responseBody.getString("avatarUrl"), AVATAR_URL
        Assert.assertEquals responseBody.getString("bio"), BIO
        Assert.assertEquals responseBody.getString("blog"), BLOG
        Assert.assertEquals responseBody.getString("company"), COMPANY
        Assert.assertEquals responseBody.getBoolean("hireable"), HIREABLE
        Assert.assertEquals responseBody.getString("email"), EMAIL
        Assert.assertEquals responseBody.getBoolean("hireable"), HIREABLE
        Assert.assertEquals responseBody.getString("location"), LOCATION
        Assert.assertEquals responseBody.getString("login"), LOGIN
        Assert.assertEquals responseBody.getString("name"), NAME
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

        userEntity.setAvatarUrl AVATAR_URL
        userEntity.setBio BIO
        userEntity.setBlog BLOG
        userEntity.setCompany COMPANY
        userEntity.setHireable HIREABLE
        userEntity.setEmail EMAIL
        userEntity.setHireable HIREABLE
        userEntity.setLocation LOCATION
        userEntity.setLogin LOGIN
        userEntity.setName NAME

        return userEntity
    }

}
