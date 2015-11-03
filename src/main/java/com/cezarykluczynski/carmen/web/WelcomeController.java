package com.cezarykluczynski.carmen.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.dao.users.CarmenUserDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.HashMap;

@Controller
public class WelcomeController {

    @Autowired
    CarmenUserDAO usersUserDAOImpl;

    @Autowired
    UserDAO githubUserDAOImpl;

    @RequestMapping("")
    public ModelAndView welcome() {
        Map<String, Object> viewVariables = new HashMap<>();

        Object analyzedUsersCount = githubUserDAOImpl.countFound();
        Object connectedUsersCount = usersUserDAOImpl.count();

        viewVariables.put("analyzedUsersCount", analyzedUsersCount + " users analyzed.");
        viewVariables.put("connectedUsersCount", connectedUsersCount + " users have connected.");

        return new ModelAndView("welcome", viewVariables);
    }
}
