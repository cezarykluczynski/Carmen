package carmen.rest.controller;

import java.io.IOException;

import java.lang.AssertionError;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;

import carmen.rest.pojo.Analysis;

import carmen.model.github.User;
import carmen.dao.github.UserDAO;

@RestController
@RequestMapping("/rest/analyze")
public class AnalysisRequestController {

    @Autowired
    UserDAO githubUserDAOImpl;

    @RequestMapping(value = "/github/{username}", method = RequestMethod.GET)
    public Analysis github(@PathVariable String username) throws AssertionError, IOException {
        User user = githubUserDAOImpl.createOrUpdate(username);
        return new Analysis(username, user.getFound() ? "found" : "not_found");
    }
}
