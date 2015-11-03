package com.cezarykluczynski.carmen.executor.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO;
import com.cezarykluczynski.carmen.client.github.GithubClient;
import com.cezarykluczynski.carmen.dao.github.UserDAO;

@Component
public class UserGhostExecutor implements Executor {

    @Autowired
    PendingRequestDAO apiqueuePendingRequestDAOImpl;

    @Autowired
    UserDAO githubUserDAOImpl;

    @Autowired
    GithubClient githubClient;

    public void execute(PendingRequest pendingRequest) throws IOException {
        String login = (String) pendingRequest.getPathParams().get("login");
        User user = githubUserDAOImpl.createOrUpdateGhostEntity(login);
        linkUsers(pendingRequest, user);
        apiqueuePendingRequestDAOImpl.delete(pendingRequest);
    }

    private void linkUsers(PendingRequest pendingRequest, User user) {
        Integer subjectId = (Integer) pendingRequest.getParams().get("link_with");
        String userLinkType = (String) pendingRequest.getParams().get("link_as");
        User subject = githubUserDAOImpl.findById(subjectId);

        if (userLinkType.equals("follower")) {
            githubUserDAOImpl.linkFollowerWithFollowee(user, subject);
        }
        if (userLinkType.equals("followee")) {
            githubUserDAOImpl.linkFollowerWithFollowee(subject, user);
        }
    }

}
