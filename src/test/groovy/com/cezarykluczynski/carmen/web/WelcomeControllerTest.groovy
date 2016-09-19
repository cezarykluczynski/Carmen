package com.cezarykluczynski.carmen.web

import com.cezarykluczynski.carmen.common.user.model.repository.CommonUserRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository
import org.springframework.web.servlet.ModelAndView
import spock.lang.Specification

class WelcomeControllerTest extends Specification {

    private static final Object CONNECTED_USERS_COUNT = 2
    private static final Object ANALYZED_CONNECTED_COUNT = 3
    private static final String VIEW_NAME = "welcome"

    private CommonUserRepository commonUserRepository

    private UserRepository userRepository

    private WelcomeController welcomeController

    def setup() {
        commonUserRepository = Mock CommonUserRepository
        userRepository = Mock UserRepository
        welcomeController = new WelcomeController(commonUserRepository, userRepository)
    }

    def welcome() {
        given:
        commonUserRepository.count() >> CONNECTED_USERS_COUNT
        userRepository.countByFoundTrue() >> ANALYZED_CONNECTED_COUNT

        when:
        ModelAndView modelAndView = welcomeController.welcome()

        then:
        modelAndView.model.get("analyzedUsersCount") == ANALYZED_CONNECTED_COUNT
        modelAndView.model.get("connectedUsersCount") == CONNECTED_USERS_COUNT
        modelAndView.viewName == VIEW_NAME
    }

}
