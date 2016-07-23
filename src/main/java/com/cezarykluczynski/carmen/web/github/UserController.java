package com.cezarykluczynski.carmen.web.github;

import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.model.github.User;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/github")
public class UserController {

    private UserDAO userDAO;

    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @RequestMapping("/{login}")
    public ModelAndView user(@PathVariable String login, HttpServletResponse response) {
        User user = userDAO.findByLogin(login);

        Map<String, Object> viewVariables = Maps.newHashMap();
        viewVariables.put("login", login);
        viewVariables.put("user", user);

        boolean userWasFound = user != null && user.isFound();
        String templateName = userWasFound ? "github/user/user" : "github/user/404";

        if (!userWasFound) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return new ModelAndView(templateName, viewVariables);
    }
}
