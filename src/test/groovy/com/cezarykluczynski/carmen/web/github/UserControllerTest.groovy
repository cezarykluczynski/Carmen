package com.cezarykluczynski.carmen.web.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.model.github.User

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
@WebAppConfiguration
class UserControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    private UserController userController

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserDAO githubUserDAOImpl

    private MockMvc mockMvc

    @BeforeMethod
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    }

    @Test
    void nonExistingUser() throws Exception {
        mockMvc.perform(get("/github/this-user-should-never-exist"))
            .andExpect(status().isNotFound())
    }

    @Test
    void existingNotFoundUser() throws Exception {
        // setup
        User userEntity = githubUserDAOImplFixtures.createNotFoundRequestedUserEntity()

        // exercise, assertion
        mockMvc.perform(get("/github/" + userEntity.getLogin()))
            .andExpect(status().isNotFound())

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void existingFoundUser() throws Exception {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // exercise, assertion
        mockMvc.perform(get("/github/" + userEntity.getLogin()))
            .andExpect(status().isOk())

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
