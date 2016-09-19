package com.cezarykluczynski.carmen.rest.controller

import com.cezarykluczynski.carmen.client.github.GithubRateLimitExceededException
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AnalysisRequestControllerTest extends Specification {

    private static final String LOGIN = "LOGIN"

    private UserRepository userRepository

    private AnalysisRequestController analysisRequestController

    private MockMvc mockMvc

    void setup() {
        userRepository = Mock UserRepository
        analysisRequestController = new AnalysisRequestController(userRepository)
        mockMvc = MockMvcBuilders.standaloneSetup(analysisRequestController).build()
    }

    def githubFoundUser() {
        given:
        User userEntity = Mock(User) {
            getLogin() >> LOGIN
            isFound() >> true
        }
        userRepository.createOrUpdateRequestedEntity(LOGIN) >> userEntity

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
        userRepository.createOrUpdateRequestedEntity(LOGIN) >> userEntity

        expect:
        mockMvc.perform(get("/rest/analyze/github/${LOGIN}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.status', is("not_found")))
            .andExpect(jsonPath('$.username', is(userEntity.getLogin())))
    }

    def githubGithubRateLimitExceededException() {
        given:
        userRepository.createOrUpdateRequestedEntity(_) >> { String login ->
            throw new GithubRateLimitExceededException()
        }

        expect:
        mockMvc.perform(get("/rest/analyze/github/${LOGIN}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.status', is("core_rate_limit_exceeded")))
            .andExpect(jsonPath('$.username', is(LOGIN)))
    }

}
