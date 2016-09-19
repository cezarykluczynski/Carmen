package com.cezarykluczynski.carmen.web.github

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository
import org.springframework.web.servlet.ModelAndView
import spock.lang.Specification

import javax.servlet.http.HttpServletResponse

class UserControllerTest extends Specification {

    private static final String LOGIN = "LOGIN"
    private static final String VIEW_NAME_FOUND = "github/user/user"
    private static final String VIEW_NAME_NOT_FOUND = "github/user/404"

    private UserRepository userRepository

    private HttpServletResponse httpServletResponseMock

    private UserController userController

    def setup() {
        userRepository = Mock UserRepository
        userController = new UserController(userRepository)
        httpServletResponseMock = Mock HttpServletResponse
    }

    def "gets found user"() {
        given:
        User user = new User(login: LOGIN, found: true)
        userRepository.findByLogin(LOGIN) >> user

        when:
        ModelAndView modelAndView = userController.user LOGIN, httpServletResponseMock

        then:
        modelAndView.model.get("login") == LOGIN
        modelAndView.model.get("user") == user
        modelAndView.viewName == VIEW_NAME_FOUND
        0 * httpServletResponseMock.setStatus(*_)
    }

    def "gets not existing found user"() {
        when:
        ModelAndView modelAndView = userController.user LOGIN, httpServletResponseMock

        then:
        modelAndView.model.get("login") == LOGIN
        modelAndView.model.get("user") == null
        modelAndView.viewName == VIEW_NAME_NOT_FOUND
        1 * httpServletResponseMock.setStatus(HttpServletResponse.SC_NOT_FOUND)
    }

    def "gets not found user"() {
        given:
        User user = new User(login: LOGIN, found: false)
        userRepository.findByLogin(LOGIN) >> user

        when:
        ModelAndView modelAndView = userController.user LOGIN, httpServletResponseMock

        then:
        modelAndView.model.get("login") == LOGIN
        modelAndView.model.get("user") == user
        modelAndView.viewName == VIEW_NAME_NOT_FOUND
        1 * httpServletResponseMock.setStatus(HttpServletResponse.SC_NOT_FOUND)
    }

}
