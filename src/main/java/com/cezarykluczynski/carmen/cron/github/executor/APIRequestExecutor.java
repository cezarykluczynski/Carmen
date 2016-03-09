package com.cezarykluczynski.carmen.cron.github.executor;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.executor.github.RepositoriesExecutor;
import com.cezarykluczynski.carmen.executor.github.UserGhostExecutor;
import com.cezarykluczynski.carmen.executor.github.UserGhostPaginatorExecutor;

@Component
public class APIRequestExecutor implements Runnable {

    private PendingRequestDAO apiqueuePendingRequestDAOImpl;

    private RepositoriesExecutor repositoriesExecutor;

    private UserGhostPaginatorExecutor userGhostPaginatorExecutor;

    private UserGhostExecutor userGhostExecutor;

    @Autowired
    public APIRequestExecutor(PendingRequestDAO apiqueuePendingRequestDAOImpl,
                              RepositoriesExecutor repositoriesExecutor,
                              UserGhostPaginatorExecutor userGhostPaginatorExecutor,
                              UserGhostExecutor userGhostExecutor) {
        this.apiqueuePendingRequestDAOImpl = apiqueuePendingRequestDAOImpl;
        this.repositoriesExecutor = repositoriesExecutor;
        this.userGhostPaginatorExecutor = userGhostPaginatorExecutor;
        this.userGhostExecutor = userGhostExecutor;
    }

    public void run() {
        try {
            PendingRequest pendingRequest = apiqueuePendingRequestDAOImpl.findMostImportantPendingRequest();
            runExecutor(pendingRequest);
        } catch (IOException e) {
        }
    }

    private void runExecutor(PendingRequest pendingRequest) throws IOException {
        String executor = pendingRequest.getExecutor();

        switch (executor) {
            case "UsersGhostPaginator":
                executeUsersGhostPaginator(pendingRequest);
                break;
            case "UserGhost":
                executeUserGhost(pendingRequest);
                break;
            case "Repositories":
                executeRepositories(pendingRequest);
                break;
            default:
                break;
        }
    }

    public void executeUsersGhostPaginator(PendingRequest pendingRequest) throws IOException {
        userGhostPaginatorExecutor.execute(pendingRequest);
    }

    public void executeUserGhost(PendingRequest pendingRequest) throws IOException {
        userGhostExecutor.execute(pendingRequest);
    }

    public void executeRepositories(PendingRequest pendingRequest) throws IOException {
        repositoriesExecutor.execute(pendingRequest);
    }

}
