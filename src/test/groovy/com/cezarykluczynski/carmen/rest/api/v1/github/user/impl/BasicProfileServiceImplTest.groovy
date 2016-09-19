package com.cezarykluczynski.carmen.rest.api.v1.github.user.impl

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository
import org.json.JSONObject
import spock.lang.Specification

import javax.ws.rs.core.Response

public class BasicProfileServiceImplTest extends Specification {

    private static final String AVATAR_URL = "avatar_url"
    private static final String BIO = "Bio"
    private static final String BLOG = "http://blog.com/"
    private static final String COMPANY = "Company"
    private static final String EMAIL = "email@example.com"
    private static final boolean HIREABLE = true
    private static final String LOCATION = "Place, Country"
    private static final String LOGIN = "username"
    private static final String NAME = "John Doe"

    private BasicProfileServiceImpl basicProfileService

    private UserRepository userRepository

    def setup() {
        userRepository = Mock UserRepository
        basicProfileService = new BasicProfileServiceImpl(userRepository)
    }

    def "existing user is shown"() {
        given:
        userRepository.findByLogin("login") >> createUserEntity()

        when:
        Response response = basicProfileService.get("login")
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        responseStatus == 200
        responseBody.getString("avatarUrl") == AVATAR_URL
        responseBody.getString("bio") == BIO
        responseBody.getString("blog") == BLOG
        responseBody.getString("company") == COMPANY
        responseBody.getBoolean("hireable") == HIREABLE
        responseBody.getString("email") == EMAIL
        responseBody.getBoolean("hireable") == HIREABLE
        responseBody.getString("location") == LOCATION
        responseBody.getString("login") == LOGIN
        responseBody.getString("name") == NAME
    }

    def "non existing user is not shown"() {
        when:
        Response response = basicProfileService.get("notALogin")
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        responseStatus == 404
        responseBody.getString("message") == "404 Not Found"
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
