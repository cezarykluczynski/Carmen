package com.cezarykluczynski.carmen.rest.controller

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.provider.github.GithubProvider
import com.cezarykluczynski.carmen.provider.github.GithubRateLimitExceededException

import static org.mockito.Mockito.when
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.thenReturn
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import static org.hamcrest.CoreMatchers.is

import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
@WebAppConfiguration
class AnalysisRequestControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Mock
    UserDAOImpl githubUserDAOImpl

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
    }

    @Test
    void githubNotFoundUser() {
        // setup
        githubUserDAOImpl = mock UserDAOImpl.class
        MockitoAnnotations.initMocks this
        String login = githubUserDAOImplFixtures.getRandomLogin()
        User userEntityMock = mock User.class
        when userEntityMock.getFound() thenReturn false
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
        String login = githubUserDAOImplFixtures.getRandomLogin()
        when githubUserDAOImpl.createOrUpdateRequestedEntity(login) thenThrow GithubRateLimitExceededException

        // exercise
        mockMvc.perform(get("/rest/analyze/github/${login}"))
        // assertion
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.status', is("core_rate_limit_exceeded")))
            .andExpect(jsonPath('$.username', is(login)))
    }

}