package com.cezarykluczynski.carmen.web

import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.users.CarmenUserDAO
import org.springframework.web.servlet.ModelAndView
import spock.lang.Specification

class WelcomeControllerTest extends Specification {

    private static final Object CONNECTED_USERS_COUNT = 2
    private static final Object ANALYZED_CONNECTED_COUNT = 3
    private static final String VIEW_NAME = "welcome"

    private CarmenUserDAO carmenUserDAOMock

    private UserDAO userDAOMock

    private WelcomeController welcomeController

    def setup() {
        carmenUserDAOMock = Mock CarmenUserDAO
        userDAOMock = Mock UserDAO
        welcomeController = new WelcomeController(carmenUserDAOMock, userDAOMock)
    }

    def welcome() {
        given:
        carmenUserDAOMock.count() >> CONNECTED_USERS_COUNT
        userDAOMock.countFound() >> ANALYZED_CONNECTED_COUNT

        when:
        ModelAndView modelAndView = welcomeController.welcome()

        then:
        modelAndView.model.get("analyzedUsersCount") == ANALYZED_CONNECTED_COUNT
        modelAndView.model.get("connectedUsersCount") == CONNECTED_USERS_COUNT
        modelAndView.viewName == VIEW_NAME

    }

}
