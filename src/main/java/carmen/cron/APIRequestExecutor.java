package carmen.cron;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import carmen.dao.apiqueue.PendingRequestDAOImpl;
import carmen.model.apiqueue.PendingRequest;
import carmen.exception.EmptyPendingRequestListException;
import carmen.executor.UserGhostPaginator;

public class APIRequestExecutor {

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

    @Autowired
    UserGhostPaginator userGhostPaginator;

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

        System.out.println("Executor: " + executor);

        switch (executor) {
            case "UsersGhostPaginator":
                userGhostPaginator.execute(pendingRequest);
                break;
            default:
                break;
        }
    }
}