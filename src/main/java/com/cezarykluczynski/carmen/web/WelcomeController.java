package com.cezarykluczynski.carmen.web;

import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.dao.users.CarmenUserDAO;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class WelcomeController {

    private CarmenUserDAO carmenUserDAO;

    private UserDAO userDAO;

    @Autowired
    public WelcomeController(CarmenUserDAO carmenUserDAO, UserDAO userDAO) {
        this.carmenUserDAO = carmenUserDAO;
        this.userDAO = userDAO;
    }

    @RequestMapping("")
    public ModelAndView welcome() {
        Map<String, Object> viewVariables = Maps.newHashMap();

        Object analyzedUsersCount = userDAO.countFound();
        Object connectedUsersCount = carmenUserDAO.count();

        viewVariables.put("analyzedUsersCount", analyzedUsersCount);
        viewVariables.put("connectedUsersCount", connectedUsersCount);

        return new ModelAndView("welcome", viewVariables);
    }
}
