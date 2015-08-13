package carmen.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import carmen.model.github.User;
import carmen.model.apiqueue.PendingRequest;
import carmen.dao.apiqueue.PendingRequestDAOImpl;
import carmen.provider.github.GithubProvider;
import carmen.dao.github.UserDAOImpl;

@Component
public class UserGhost implements Executor {

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Autowired
    GithubProvider githubProvider;

    public void execute(PendingRequest pendingRequest) throws IOException {
        String login = (String) pendingRequest.getPathParams().get("login");
        User user = githubUserDAOImpl.createOrUpdateGhostEntity(login);
        linkUsers(pendingRequest, user);
        apiqueuePendingRequestDao.delete(pendingRequest);
    }

    private void linkUsers(PendingRequest pendingRequest, User user) {
        Integer subjectId = (Integer) pendingRequest.getParams().get("link_with");
        String userLinkType = (String) pendingRequest.getParams().get("link_as");
        User subject = githubUserDAOImpl.findById(subjectId);

        if (userLinkType.equals("follower")) {
            githubUserDAOImpl.linkFollowerWithFollowee(user, subject);
        } else if (userLinkType.equals("followee")) {
            githubUserDAOImpl.linkFollowerWithFollowee(subject, user);
        }
    }

}
