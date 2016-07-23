package com.cezarykluczynski.carmen.web

import org.springframework.web.servlet.ModelAndView
import spock.lang.Specification


class AdminPanelControllerTest extends Specification {

    private static final String VIEW_NAME = "admin/panel"

    private AdminPanelController adminPanelController

    def setup() {
        adminPanelController = new AdminPanelController()
    }

    def "gets template"() {
        when:
        ModelAndView modelAndView = adminPanelController.adminPanel()

        then:
        modelAndView.model.isEmpty()
        modelAndView.viewName == VIEW_NAME
    }


}