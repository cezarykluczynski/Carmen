package com.cezarykluczynski.carmen.rest.controller;

import java.io.IOException;

import java.lang.AssertionError;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;

import com.cezarykluczynski.carmen.rest.pojo.Analysis;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.provider.github.GithubRateLimitExceededException;

@RestController
@RequestMapping("/rest/analyze")
public class AnalysisRequestController {

    @Autowired
    UserDAO githubUserDAOImpl;

    @RequestMapping(value = "/github/{login}", method = RequestMethod.GET)
    public Analysis github(@PathVariable String login) throws AssertionError, IOException {
        try {
            User user = githubUserDAOImpl.createOrUpdateRequestedEntity(login);
            return new Analysis(login, user.getFound() ? "found" : "not_found");
        } catch(GithubRateLimitExceededException e) {
            return new Analysis(login, "core_rate_limit_exceeded");
        }
    }

}
