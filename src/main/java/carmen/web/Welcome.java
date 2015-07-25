package carmen.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Welcome {

	@RequestMapping("")
	public ModelAndView welcome() {
		return new ModelAndView("welcome", "message", "Hello world");
	}
}
