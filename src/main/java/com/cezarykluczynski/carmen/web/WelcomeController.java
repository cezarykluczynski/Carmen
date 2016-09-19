package com.cezarykluczynski.carmen.web;

import com.cezarykluczynski.carmen.common.user.model.repository.CommonUserRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class WelcomeController {

    private CommonUserRepository commonUserRepository;

    private UserRepository userRepository;

    @Autowired
    public WelcomeController(CommonUserRepository commonUserRepository, UserRepository userRepository) {
        this.commonUserRepository = commonUserRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping("")
    public ModelAndView welcome() {
        Map<String, Object> viewVariables = Maps.newHashMap();

        Object analyzedUsersCount = userRepository.countByFoundTrue();
        Object connectedUsersCount = commonUserRepository.count();

        viewVariables.put("analyzedUsersCount", analyzedUsersCount);
        viewVariables.put("connectedUsersCount", connectedUsersCount);

        return new ModelAndView("welcome", viewVariables);
    }

}
