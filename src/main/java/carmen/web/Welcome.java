package carmen.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import carmen.dao.users.UserDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.HashMap;

@Controller
public class Welcome {

    @Autowired
    UserDAOImpl usersUserDAOImpl;

    @Autowired
    carmen.dao.github.UserDAOImpl githubUserDAOImpl;

    @RequestMapping("")
    public ModelAndView welcome() {
        Map<String, Object> viewVariables = new HashMap<>();

        Object usersUserCount = usersUserDAOImpl.count();
        Object githubUserCount = githubUserDAOImpl.countFound();

        viewVariables.put("usersCount", githubUserCount + " users analyzed. " + usersUserCount + " users have connected.");

        return new ModelAndView("welcome", viewVariables);
    }
}
