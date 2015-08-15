package com.cezarykluczynski.carmen.executor;

import java.io.IOException;

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;

public interface Executor {

    public void execute(PendingRequest pendingRequest) throws IOException;

}