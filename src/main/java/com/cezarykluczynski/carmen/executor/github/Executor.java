package com.cezarykluczynski.carmen.executor.github;

import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest;

import java.io.IOException;

public interface Executor {

    void execute(PendingRequest pendingRequest) throws IOException;

}
