package com.cezarykluczynski.carmen.rest.controller

import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.client.github.GithubRateLimitExceededException

import static org.hamcrest.CoreMatchers.is

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class AnalysisRequestControllerTest extends Specification {

    private static final String LOGIN = "LOGIN"

    private UserDAO userDAOMock

    private AnalysisRequestController analysisRequestController

    private MockMvc mockMvc

    void setup() {
        userDAOMock = Mock UserDAO
        analysisRequestController = new AnalysisRequestController(userDAOMock)
        mockMvc = MockMvcBuilders.standaloneSetup(analysisRequestController).build()
    }

    def githubFoundUser() {
        given:
        User userEntity = Mock(User) {
            getLogin() >> LOGIN
            isFound() >> true
        }
        userDAOMock.createOrUpdateRequestedEntity(LOGIN) >> userEntity

        expect:
        mockMvc.perform(get("/rest/analyze/github/${userEntity.getLogin()}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.status', is("found")))
            .andExpect(jsonPath('$.username', is(userEntity.getLogin())))
    }

    void githubNotFoundUser() {
        given:
        User userEntity = Mock(User) {
            getLogin() >> LOGIN
            isFound() >> false
        }
        userDAOMock.createOrUpdateRequestedEntity(LOGIN) >> userEntity

        expect:
        mockMvc.perform(get("/rest/analyze/github/${LOGIN}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.status', is("not_found")))
            .andExpect(jsonPath('$.username', is(userEntity.getLogin())))
    }

    def githubGithubRateLimitExceededException() {
        given:
        userDAOMock.createOrUpdateRequestedEntity(_) >> { String login ->
            throw new GithubRateLimitExceededException()
        }

        expect:
        mockMvc.perform(get("/rest/analyze/github/${LOGIN}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.status', is("core_rate_limit_exceeded")))
            .andExpect(jsonPath('$.username', is(LOGIN)))
    }

}
