package carmen.rest.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;

import carmen.dao.UserDAOImpl;
import carmen.rest.pojo.Analysis;

@RestController
@RequestMapping("/rest/analyze")
public class AnalysisRequestController {

    @RequestMapping(value = "/github/{username}", method = RequestMethod.GET)
    public Analysis github(
        @PathVariable String username
    ) {
        return new Analysis(username, "unknown");
    }
}
