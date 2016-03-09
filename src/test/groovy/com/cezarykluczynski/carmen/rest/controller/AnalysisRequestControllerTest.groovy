package com.cezarykluczynski.carmen.rest.controller

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.client.github.GithubRateLimitExceededException

import static org.mockito.Mockito.when
import static org.mockito.Mockito.mock
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import static org.hamcrest.CoreMatchers.is

import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class AnalysisRequestControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    @Mock
    UserDAO githubUserDAOImpl

    @Autowired
    @InjectMocks
    private AnalysisRequestController analysisRequestController

    private MockMvc mockMvc

    @BeforeMethod
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(analysisRequestController).build()
    }

    @Test
    void githubFoundUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // exercise
        mockMvc.perform(get("/rest/analyze/github/${userEntity.getLogin()}"))
        // assertion
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.status', is("found")))
            .andExpect(jsonPath('$.username', is(userEntity.getLogin())))

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntityByUserEntity userEntity
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntityByUserEntity userEntity
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void githubNotFoundUser() {
        // setup
        githubUserDAOImpl = mock UserDAOImpl.class
        MockitoAnnotations.initMocks this
        String login = githubUserDAOImplFixtures.generateRandomLogin()
        User userEntityMock = mock User.class
        when userEntityMock.isFound() thenReturn false
        when githubUserDAOImpl.createOrUpdateRequestedEntity(login) thenReturn userEntityMock

        // exercise
        mockMvc.perform(get("/rest/analyze/github/${login}"))
        // assertion
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.status', is("not_found")))
            .andExpect(jsonPath('$.username', is(login)))
    }

    @Test
    void githubGithubRateLimitExceededException() {
        // setup
        githubUserDAOImpl = mock UserDAOImpl.class
        MockitoAnnotations.initMocks this
        String login = githubUserDAOImplFixtures.generateRandomLogin()
        when githubUserDAOImpl.createOrUpdateRequestedEntity(login) thenThrow GithubRateLimitExceededException

        // exercise
        mockMvc.perform(get("/rest/analyze/github/${login}"))
        // assertion
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.status', is("core_rate_limit_exceeded")))
            .andExpect(jsonPath('$.username', is(login)))
    }

}
