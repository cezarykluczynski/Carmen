package carmen.cron;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import carmen.dao.apiqueue.PendingRequestDAOImpl;
import carmen.model.apiqueue.PendingRequest;
import carmen.exception.EmptyPendingRequestListException;
import carmen.executor.UserGhostPaginator;
import carmen.executor.UserGhost;

public class APIRequestExecutor {

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

    @Autowired
    UserGhostPaginator userGhostPaginator;

    @Autowired
    UserGhost userGhost;

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

    private void runExecutor(PendingRequest pendingRequest) throws IOException {
        String executor = pendingRequest.getExecutor();

        switch (executor) {
            case "UsersGhostPaginator":
                userGhostPaginator.execute(pendingRequest);
                break;
            case "UserGhost":
                userGhost.execute(pendingRequest);
                break;
            default:
                break;
        }
    }
}