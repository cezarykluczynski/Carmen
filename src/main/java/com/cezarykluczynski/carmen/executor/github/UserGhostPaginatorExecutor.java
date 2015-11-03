package com.cezarykluczynski.carmen.executor.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO;
import com.cezarykluczynski.carmen.client.github.GithubClient;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

@Component
public class UserGhostPaginatorExecutor implements Executor {

    @Autowired
    PendingRequestDAO apiqueuePendingRequestDAOImpl;

    @Autowired
    GithubClient githubClient;

    @Value("${executor.UserGhostPaginatorExecutor.paginationLimit}")
    private Integer limit;

    public void execute(PendingRequest pendingRequest) throws IOException {
        PaginationAwareArrayList<User> users = getUserListFromPendingRequest(pendingRequest);

        if (users.size() > 0) {
            createUserGhostPendingRequests(users, pendingRequest);
        }

        if (users.isLastPage()) {
            apiqueuePendingRequestDAOImpl.delete(pendingRequest);
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

        return endpoint.equals("followers_url") ?
            githubClient.getFollowers(login, limit, page) :
            githubClient.getFollowing(login, limit, page);
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
            apiqueuePendingRequestDAOImpl.create(
                "UserGhost",
                null,
                pathParams,
                queryParams,
                params,
                pendingRequest.getPropagationId(),
                1
            );
        }
    }

    private void continuePagination(PendingRequest pendingRequest, PaginationAwareArrayList<User> users) {
        HashMap<String, Object> queryParams = pendingRequest.getQueryParams();
        queryParams.put("page", users.getNextPage());
        pendingRequest.setQueryParams(queryParams);
        apiqueuePendingRequestDAOImpl.update(pendingRequest);
    }

    private String linkAsRole(PendingRequest pendingRequest) {
        String endpoint = (String) pendingRequest.getPathParams().get("endpoint");
        return endpoint.equals("followers_url") ? "follower" : "followee";
    }

}
