package carmen.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import carmen.dao.UserDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.HashMap;

@Controller
public class Welcome {

    @Autowired
    UserDAOImpl userDAOImpl;

    @RequestMapping("")
    public ModelAndView welcome() {
        Map<String, Object> viewVariables = new HashMap<>();

        Object userCount = this.userDAOImpl.count();

        viewVariables.put("message", "There are " + userCount + " users.");

        return new ModelAndView("welcome", viewVariables);
    }
}
