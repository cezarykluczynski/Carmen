package com.cezarykluczynski.carmen.web.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.model.github.User

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
@WebAppConfiguration
class UserControllerTest {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    private UserController userController

    @Autowired
    UserDAOImpl githubUserDAOImpl

    private MockMvc mockMvc

    @Before
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
        User userEntity = createUser(false)

        // exercise, assertion
        mockMvc.perform(get("/github/" + userEntity.getLogin()))
            .andExpect(status().isNotFound())

        // teardown
        deleteUserEntity(userEntity)
    }

    @Test
    void existingFoundUser() throws Exception {
        // setup
        User userEntity = createUser(true)

        // exercise, assertion
        mockMvc.perform(get("/github/" + userEntity.getLogin()))
            .andExpect(status().isOk())

        // teardown
        deleteUserEntity(userEntity)
    }

    private User createUser(Boolean found) {
        String login = "random_login" + System.currentTimeMillis()
        com.cezarykluczynski.carmen.set.github.User mockedUserSet = mock(com.cezarykluczynski.carmen.set.github.User.class)
        when(mockedUserSet.getLogin()).thenReturn(login)
        when(mockedUserSet.getRequested()).thenReturn(true)
        User userEntity = githubUserDAOImpl.create(mockedUserSet)
        userEntity.setFound(found)
        Session session = sessionFactory.openSession()
        session.update(userEntity)
        session.flush()
        session.close()
        return userEntity
    }

    private void deleteUserEntity(User userEntity) {
        Session session = sessionFactory.openSession()
        session.delete(userEntity)
        session.flush()
        session.close()
    }

}
