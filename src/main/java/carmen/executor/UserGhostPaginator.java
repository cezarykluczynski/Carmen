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
import carmen.util.PaginationAwareArrayList;

@Component
public class UserGhostPaginator implements Executor {

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

    @Autowired
    GithubProvider githubProvider;

    // TODO: move to config
    private Integer limit = 50;

    public void execute(PendingRequest pendingRequest) throws IOException {
        PaginationAwareArrayList<User> users = getUserListFromPendingRequest(pendingRequest);

        if (users.size() > 0) {
            createUserGhostPendingRequests(users);
        }

        if (users.isLastPage()) {
            apiqueuePendingRequestDao.delete(pendingRequest);
        } else {
            continuePagination(pendingRequest, users);
        }
    }

    private PaginationAwareArrayList<User> getUserListFromPendingRequest(PendingRequest pendingRequest) throws IOException {
        HashMap<String, Object> pathParams = pendingRequest.getPathParams();
        HashMap<String, Object> queryParams = pendingRequest.getQueryParams();

        String login = (String) pathParams.get("login");
        String endpoint = (String) pathParams.get("endpoint");
        Integer page = (Integer) (queryParams.containsKey("page") ? queryParams.get("page") : 1);

        if (endpoint.equals("followers_url")) {
            return githubProvider.getFollowers(login, limit, page);
        } else if (endpoint.equals("following_url")) {
            return githubProvider.getFollowing(login, limit, page);
        } else {
            throw new IOException("Endpoint " + endpoint + " not implemented.");
        }
    }

    private void createUserGhostPendingRequests(PaginationAwareArrayList<User> users) {
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        HashMap<String, Object> queryParams = new HashMap<String, Object>();

        for (User user : users) {
            pathParams.put("login", user.getLogin());
            apiqueuePendingRequestDao.create("UserGhost", null, pathParams, queryParams, 1);
        }
    }

    private void continuePagination(PendingRequest pendingRequest, PaginationAwareArrayList<User> users) {
        pendingRequest.getQueryParams().put("page", users.getNextPage());
        apiqueuePendingRequestDao.update(pendingRequest);
    }

}
