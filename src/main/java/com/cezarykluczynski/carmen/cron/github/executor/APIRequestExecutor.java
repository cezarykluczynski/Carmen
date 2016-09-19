package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest;
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository;
import com.cezarykluczynski.carmen.executor.github.RepositoriesExecutor;
import com.cezarykluczynski.carmen.executor.github.UserGhostExecutor;
import com.cezarykluczynski.carmen.executor.github.UserGhostPaginatorExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@DatabaseSwitchableJob
public class APIRequestExecutor implements Runnable {

    private PendingRequestRepository pendingRequestRepository;

    private RepositoriesExecutor repositoriesExecutor;

    private UserGhostPaginatorExecutor userGhostPaginatorExecutor;

    private UserGhostExecutor userGhostExecutor;

    @Autowired
    public APIRequestExecutor(PendingRequestRepository pendingRequestRepository,
                              RepositoriesExecutor repositoriesExecutor,
                              UserGhostPaginatorExecutor userGhostPaginatorExecutor,
                              UserGhostExecutor userGhostExecutor) {
        this.pendingRequestRepository = pendingRequestRepository;
        this.repositoriesExecutor = repositoriesExecutor;
        this.userGhostPaginatorExecutor = userGhostPaginatorExecutor;
        this.userGhostExecutor = userGhostExecutor;
    }

    public void run() {
        PendingRequest pendingRequest = pendingRequestRepository.findMostImportantPendingRequest();
        if (pendingRequest != null) {
            runExecutor(pendingRequest);
        }
    }

    private void runExecutor(PendingRequest pendingRequest) {
        String executor = pendingRequest.getExecutor();

        switch (executor) {
            case "Repositories":
                executeRepositories(pendingRequest);
                break;
            case "UsersGhostPaginator":
                executeUsersGhostPaginator(pendingRequest);
                break;
            case "UserGhost":
                executeUserGhost(pendingRequest);
                break;
            default:
                break;
        }
    }

    private void executeRepositories(PendingRequest pendingRequest) {
        try {
            repositoriesExecutor.execute(pendingRequest);
        } catch (IOException e) {
        }
    }

    private void executeUsersGhostPaginator(PendingRequest pendingRequest) {
        try {
            userGhostPaginatorExecutor.execute(pendingRequest);
        } catch (IOException e) {
        }
    }

    private void executeUserGhost(PendingRequest pendingRequest) {
        try {
            userGhostExecutor.execute(pendingRequest);
        } catch (IOException e) {
        }
    }

}
