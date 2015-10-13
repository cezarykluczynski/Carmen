package com.cezarykluczynski.carmen.rest.api.v1.github.user

import com.jayway.restassured.RestAssured
import static com.jayway.restassured.RestAssured.expect
import static com.jayway.restassured.matcher.RestAssuredMatchers.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import static org.hamcrest.Matchers.equalTo

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures

import org.testng.annotations.Test

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class BasicProfileServiceImplIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Test
    void foundUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // assertion, exercise
        expect()
        .body(
            "login", equalTo(userEntity.getLogin()),
        )
        .when()
        .get("http://localhost:8080/Carmen/api/v1/github/user/${userEntity.getLogin()}/basicProfile")

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void notFoundUser() {
        expect()
        .body(
            "message", equalTo("404 Not Found"),
        )
        .when()
        .get("http://localhost:8080/Carmen/api/v1/github/user/not-found-user/basicProfile")
    }

}
