package carmen.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import carmen.dao.UserDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Controller
public class Welcome {

    @Autowired
    UserDAOImpl userDAOImpl;

    @RequestMapping("")
    public ModelAndView welcome() {
        Object count = this.userDAOImpl.count();
        return new ModelAndView("welcome", "message", "There are " + count + " users." );
    }
}
