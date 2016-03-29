package com.cezarykluczynski.carmen.web;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
@Profile("admin-panel")
public class AdminPanelController {

    @RequestMapping("/panel")
    public ModelAndView user() {
        return new ModelAndView("admin/panel");
    }

}
