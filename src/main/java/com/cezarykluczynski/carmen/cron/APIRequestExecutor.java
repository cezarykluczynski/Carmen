package com.cezarykluczynski.carmen.cron;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.exception.EmptyPendingRequestListException;
import com.cezarykluczynski.carmen.executor.UserGhostPaginatorExecutor;
import com.cezarykluczynski.carmen.executor.UserGhostExecutor;

@Component
public class APIRequestExecutor {

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

    @Autowired
    UserGhostPaginatorExecutor userGhostPaginator;

    @Autowired
    UserGhostExecutor userGhost;

    public void run() {
        try {
            PendingRequest pendingRequest = apiqueuePendingRequestDao.findMostImportantPendingRequest();
            runExecutor(pendingRequest);
        } catch (EmptyPendingRequestListException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }

    public void runExecutor(PendingRequest pendingRequest) throws IOException {
        String executor = pendingRequest.getExecutor();

        switch (executor) {
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

    public void executeUsersGhostPaginator(PendingRequest pendingRequest) throws IOException {
        userGhostPaginator.execute(pendingRequest);
    }

    public void executeUserGhost(PendingRequest pendingRequest) throws IOException {
        userGhost.execute(pendingRequest);
    }

}