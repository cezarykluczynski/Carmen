package carmen.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import carmen.dao.UserDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class Welcome {

    @Autowired
    UserDAOImpl userDAOImpl;

    @RequestMapping("")
    public ModelAndView welcome() {
        Object userCount = this.userDAOImpl.count();
        return new ModelAndView("welcome", "message", "There are " + userCount + " users." );
    }
}
