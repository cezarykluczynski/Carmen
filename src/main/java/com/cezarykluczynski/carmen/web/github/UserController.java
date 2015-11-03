package com.cezarykluczynski.carmen.web.github;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.dao.users.CarmenUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import com.cezarykluczynski.carmen.model.github.User;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;

@Controller
@RequestMapping("/github")
public class UserController {

    @Autowired
    CarmenUserDAO usersUserDAOImpl;

    @Autowired
    UserDAO githubUserDAOImpl;

    @RequestMapping("/{login}")
    public ModelAndView user(@PathVariable String login, HttpServletResponse response) {
        User user = githubUserDAOImpl.findByLogin(login);

        Map<String, Object> viewVariables = new HashMap<>();
        viewVariables.put("login", login);
        viewVariables.put("user", user);

        Boolean userWasFound = user instanceof User && user.getFound();
        String templateName = userWasFound ? "github/user/user" : "github/user/404";

        if (!userWasFound) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return new ModelAndView(templateName, viewVariables);
    }
}
