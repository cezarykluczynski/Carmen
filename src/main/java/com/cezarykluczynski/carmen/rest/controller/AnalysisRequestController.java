package com.cezarykluczynski.carmen.rest.controller;

import com.cezarykluczynski.carmen.client.github.GithubRateLimitExceededException;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository;
import com.cezarykluczynski.carmen.rest.dto.Analysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/rest/analyze")
public class AnalysisRequestController {

    private UserRepository userRepository;

    @Autowired
    public AnalysisRequestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/github/{login}", method = RequestMethod.GET)
    public Analysis github(@PathVariable String login) throws IOException {
        try {
            User user = userRepository.createOrUpdateRequestedEntity(login);
            return new Analysis(login, user.isFound() ? "found" : "not_found");
        } catch(GithubRateLimitExceededException e) {
            return new Analysis(login, "core_rate_limit_exceeded");
        }
    }

}
