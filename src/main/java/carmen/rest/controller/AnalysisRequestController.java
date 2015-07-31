package carmen.rest.controller;

import java.io.IOException;

import java.lang.AssertionError;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;

import carmen.provider.github.GithubProvider;
import carmen.rest.pojo.Analysis;

import carmen.set.github.User;

@RestController
@RequestMapping("/rest/analyze")
public class AnalysisRequestController {

    @Autowired
    GithubProvider githubProvider;

    @RequestMapping(value = "/github/{username}", method = RequestMethod.GET)
    public Analysis github(@PathVariable String username) throws AssertionError, IOException {
        User user = githubProvider.getUser(username);
        String status = user.exists() ? "found" : "not_found";

        if (user.exists()) {

        }

        return new Analysis(username, status);
    }
}
