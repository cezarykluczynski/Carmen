package com.cezarykluczynski.carmen.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl;
import com.cezarykluczynski.carmen.provider.github.GithubProvider;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

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
            createUserGhostPendingRequests(users, pendingRequest);
        }

        if (users.isLastPage()) {
            apiqueuePendingRequestDao.delete(pendingRequest);
        } else {
            continuePagination(pendingRequest, users);
        }
    }

    private PaginationAwareArrayList<User> getUserListFromPendingRequest(
        PendingRequest pendingRequest
    ) throws IOException {
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

    private void createUserGhostPendingRequests(
        PaginationAwareArrayList<User> users,
        PendingRequest pendingRequest
    ) throws IOException {
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("link_with", pendingRequest.getUser().getId());
        params.put("link_as", linkAsRole(pendingRequest));

        for (User user : users) {
            pathParams.put("login", user.getLogin());
            apiqueuePendingRequestDao.create("UserGhost", null, pathParams, queryParams, params, 1);
        }
    }

    private void continuePagination(PendingRequest pendingRequest, PaginationAwareArrayList<User> users) {
        pendingRequest.getQueryParams().put("page", users.getNextPage());
        apiqueuePendingRequestDao.update(pendingRequest);
    }

    private String linkAsRole(PendingRequest pendingRequest) throws IOException {
        String endpoint = (String) pendingRequest.getPathParams().get("endpoint");

        if (endpoint.equals("followers_url")) {
            return "follower";
        } else if (endpoint.equals("following_url")) {
            return "followee";
        } else {
            throw new IOException("Endpoint " + endpoint + " not implemented.");
        }
    }

}
