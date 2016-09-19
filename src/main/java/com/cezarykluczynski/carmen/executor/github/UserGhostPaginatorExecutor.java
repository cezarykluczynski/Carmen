package com.cezarykluczynski.carmen.executor.github;

import com.cezarykluczynski.carmen.client.github.GithubClient;
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest;
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository;
import com.cezarykluczynski.carmen.set.github.UserDTO;
import com.cezarykluczynski.carmen.util.DateTimeConstants;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class UserGhostPaginatorExecutor implements Executor {

    private PendingRequestRepository pendingRequestRepository;

    private GithubClient githubClient;

    private Integer paginationLimit;

    @Autowired
    public UserGhostPaginatorExecutor(PendingRequestRepository pendingRequestRepository, GithubClient githubClient,
                          @Value("${executor.UserGhostPaginatorExecutor.paginationLimit}") Integer paginationLimit) {
        this.pendingRequestRepository = pendingRequestRepository;
        this.githubClient = githubClient;
        this.paginationLimit = paginationLimit;
    }

    public void execute(PendingRequest pendingRequest) throws IOException {
        if (isPendingRequestOnFirstPage(pendingRequest) && isPendingRequestBlocked(pendingRequest)) {
            pendingRequestRepository.postponeRequest(pendingRequest, DateTimeConstants.MILLISECONDS_IN_HOUR);
        } else {
            doExecutePendingRequest(pendingRequest);
        }
    }

    private void doExecutePendingRequest(PendingRequest pendingRequest) throws IOException {
        PaginationAwareArrayList<UserDTO> userDTOs = getUserListFromPendingRequest(pendingRequest);

        if (userDTOs.size() > 0) {
            createUserGhostPendingRequests(userDTOs, pendingRequest);
        }

        if (userDTOs.isLastPage()) {
            pendingRequestRepository.delete(pendingRequest);
        } else {
            continuePagination(pendingRequest, userDTOs);
        }
    }

    private PaginationAwareArrayList<UserDTO> getUserListFromPendingRequest(PendingRequest pendingRequest)
            throws IOException {
        HashMap<String, Object> pathParams = pendingRequest.getPathParams();

        String login = (String) pathParams.get("login");
        String endpoint = (String) pathParams.get("endpoint");
        Integer page = getPageFromPendingRequest(pendingRequest);

        return endpoint.equals("followers_url") ?
            githubClient.getFollowers(login, paginationLimit, page) :
            githubClient.getFollowing(login, paginationLimit, page);
    }

    private void createUserGhostPendingRequests(PaginationAwareArrayList<UserDTO> userDTOs,
            PendingRequest pendingRequest) throws IOException {
        HashMap<String, Object> pathParams = Maps.newHashMap();
        HashMap<String, Object> queryParams = Maps.newHashMap();
        HashMap<String, Object> params = Maps.newHashMap();

        params.put("link_with", pendingRequest.getUser().getId());
        params.put("link_as", convertPendingRequestToRole(pendingRequest));

        for (UserDTO userDTO : userDTOs) {
            pathParams.put("login", userDTO.getLogin());
            pendingRequestRepository.create(
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

    private void continuePagination(PendingRequest pendingRequest, PaginationAwareArrayList<UserDTO> userDTOs) {
        HashMap<String, Object> queryParams = pendingRequest.getQueryParams();
        queryParams.put("page", userDTOs.getNextPage());
        pendingRequest.setQueryParams(queryParams);
        pendingRequestRepository.save(pendingRequest);
    }

    private Integer getPageFromPendingRequest(PendingRequest pendingRequest) {
        HashMap<String, Object> queryParams = pendingRequest.getQueryParams();
        return (Integer)(queryParams.containsKey("page") ? queryParams.get("page") : 1);
    }

    private boolean isPendingRequestOnFirstPage(PendingRequest pendingRequest) {
        return getPageFromPendingRequest(pendingRequest).equals(1);
    }

    private boolean isPendingRequestBlocked(PendingRequest pendingRequest) {
        com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User userEntity =
                pendingRequest.getUser();
        return pendingRequestRepository.userEntityFollowersRequestIsBlocked(userEntity);
    }

    private String convertPendingRequestToRole(PendingRequest pendingRequest) {
        String endpoint = (String) pendingRequest.getPathParams().get("endpoint");
        return endpoint.equals("followers_url") ? "follower" : "followee";
    }

}
