package carmen.dao.apiqueue;

import carmen.model.apiqueue.PendingRequest;
import carmen.model.github.User;
import carmen.exception.EmptyPendingRequestListException;

import java.util.HashMap;
import java.util.List;

public interface PendingRequestDAO {

    public List<PendingRequest> findByUser(User userEntity);

    public PendingRequest create(
        String executor,
        User userEntity,
        HashMap<String, Object> pathParams,
        HashMap<String, Object> queryParams,
        Integer priotity
    );

    public PendingRequest findMostImportantPendingRequest() throws EmptyPendingRequestListException;

    public void update(PendingRequest pendingRequest);

    public void delete(PendingRequest pendingRequest);

}
