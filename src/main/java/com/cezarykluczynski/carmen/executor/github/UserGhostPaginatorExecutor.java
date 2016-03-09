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
import com.cezarykluczynski.carmen.util.DateTimeConstants;

@Component
public class UserGhostPaginatorExecutor implements Executor {

    private PendingRequestDAO apiqueuePendingRequestDAOImpl;

    private GithubClient githubClient;

    private Integer paginationLimit;

    @Autowired
    public UserGhostPaginatorExecutor(PendingRequestDAO apiqueuePendingRequestDAOImpl, GithubClient githubClient,
                          @Value("${executor.UserGhostPaginatorExecutor.paginationLimit}") Integer paginationLimit) {
        this.apiqueuePendingRequestDAOImpl = apiqueuePendingRequestDAOImpl;
        this.githubClient = githubClient;
        this.paginationLimit = paginationLimit;
    }

    public void execute(PendingRequest pendingRequest) throws IOException {
        if (isPendingRequestOnFirstPage(pendingRequest) && isPendingRequestBlocked(pendingRequest)) {
            apiqueuePendingRequestDAOImpl.postponeRequest(pendingRequest, DateTimeConstants.MILLISECONDS_IN_HOUR);
        } else {
            doExecutePendingRequest(pendingRequest);
        }
    }

    private void doExecutePendingRequest(PendingRequest pendingRequest) throws IOException {
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

        String login = (String) pathParams.get("login");
        String endpoint = (String) pathParams.get("endpoint");
        Integer page = getPageFromPendingRequest(pendingRequest);

        return endpoint.equals("followers_url") ?
            githubClient.getFollowers(login, paginationLimit, page) :
            githubClient.getFollowing(login, paginationLimit, page);
    }

    private void createUserGhostPendingRequests(
        PaginationAwareArrayList<User> users,
        PendingRequest pendingRequest
    ) throws IOException {
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("link_with", pendingRequest.getUser().getId());
        params.put("link_as", convertPendingRequestToRole(pendingRequest));

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

    private Integer getPageFromPendingRequest(PendingRequest pendingRequest) {
        HashMap<String, Object> queryParams = pendingRequest.getQueryParams();
        return (Integer)(queryParams.containsKey("page") ? queryParams.get("page") : 1);
    }

    private boolean isPendingRequestOnFirstPage(PendingRequest pendingRequest) {
        return getPageFromPendingRequest(pendingRequest).equals(1);
    }

    private boolean isPendingRequestBlocked(PendingRequest pendingRequest) {
        com.cezarykluczynski.carmen.model.github.User userEntity = pendingRequest.getUser();
        return apiqueuePendingRequestDAOImpl.userEntityFollowersRequestIsBlocked(userEntity);
    }

    private String convertPendingRequestToRole(PendingRequest pendingRequest) {
        String endpoint = (String) pendingRequest.getPathParams().get("endpoint");
        return endpoint.equals("followers_url") ? "follower" : "followee";
    }

}
