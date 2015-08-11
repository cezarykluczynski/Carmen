package carmen.executor;

import java.io.IOException;

import carmen.model.apiqueue.PendingRequest;

public interface Executor {

    public void execute(PendingRequest pendingRequest) throws IOException;

}