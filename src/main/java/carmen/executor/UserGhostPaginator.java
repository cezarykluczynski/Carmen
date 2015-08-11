package carmen.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import carmen.set.github.User;

import carmen.model.apiqueue.PendingRequest;
import carmen.dao.apiqueue.PendingRequestDAOImpl;
import carmen.provider.github.GithubProvider;

@Component
public class UserGhostPaginator implements Executor {

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

    @Autowired
    GithubProvider githubProvider;

    // TODO: move to config
    private Integer limit = 50;

    public void execute(PendingRequest pendingRequest) throws IOException {
        ArrayList<User> users = getUserListFromPendingRequest(pendingRequest);

        if (users.size() > 0) {
            createUserGhostPendingRequests(users);
        }

        if (users.size() < limit) {
            apiqueuePendingRequestDao.delete(pendingRequest);
        }
    }

    public ArrayList<User> getUserListFromPendingRequest(PendingRequest pendingRequest) throws IOException {
        String login = (String) pendingRequest.getPathParams().get("login");
        String endpoint = (String) pendingRequest.getPathParams().get("endpoint");

        if (endpoint.equals("followers_url")) {
            return githubProvider.getFollowers(login, limit, 0);
        } else if (endpoint.equals("following_url")) {
            return githubProvider.getFollowing(login, limit, 0);
        } else {
            throw new IOException("Endpoint not implemented.");
        }
    }

    private void createUserGhostPendingRequests(ArrayList<User> users) {
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        HashMap<String, Object> queryParams = new HashMap<String, Object>();

        for (User user : users) {
            pathParams.put("login", user.getLogin());
            apiqueuePendingRequestDao.create("UserGhost", null, pathParams, queryParams, 1);
        }
    }

}
