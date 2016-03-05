package com.cezarykluczynski.carmen.rest.api.v1.github.user

import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.model.github.User
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.JerseyTestNg
import org.glassfish.jersey.test.TestProperties
import org.json.JSONObject
import org.testng.Assert
import org.testng.annotations.Test

import javax.ws.rs.core.Application
import javax.ws.rs.core.Response

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class BasicProfileServiceImplTest extends JerseyTestNg.ContainerPerClassTest {

    private static final String avatarUrl = "avatar_url"
    private static final String bio = "Bio"
    private static final String blog = "http://blog.com/"
    private static final String company = "Compoany"
    private static final String email = "email@example.com"
    private static final boolean hireable = true
    private static final String location = "Place, Country"
    private static final String login = "username"
    private static final String name = "John Doe"

    @Override
    protected Application configure() {
        enable TestProperties.LOG_TRAFFIC
        enable TestProperties.CONTAINER_FACTORY
        enable TestProperties.DUMP_ENTITY

        UserDAO githubUserDAOImpl = mock UserDAO.class
        when githubUserDAOImpl.findByLogin("login") thenReturn createUserEntity()
        BasicProfileServiceImpl basicProfileService = new BasicProfileServiceImpl(githubUserDAOImpl)
        return new ResourceConfig().registerInstances(basicProfileService)
    }

    @Override
    Object getProperty(String property) {
        return super.getProperty(property)
    }

    @Test
    public void "existing user is shown"() {
        Response response = target().path("/v1/github/user/login/basicProfile").request().get()
        BufferedInputStream entity = (BufferedInputStream) response.getEntity()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(entity.getText())

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
        Response response = target().path("/v1/github/user/notALogin/basicProfile").request().get()
        BufferedInputStream entity = (BufferedInputStream) response.getEntity()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(entity.getText())

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
