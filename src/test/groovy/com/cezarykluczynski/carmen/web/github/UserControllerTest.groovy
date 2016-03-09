package com.cezarykluczynski.carmen.web.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.model.github.User

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class UserControllerTest extends AbstractTestNGSpringContextTests {

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
