package com.cezarykluczynski.carmen.vcs.git.worker;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository;
import com.cezarykluczynski.carmen.util.exec.result.Result;

public abstract class AbstractCloneWorker implements CloneWorker {

    protected abstract Result clone(Repository repositoryEntity, String cloneDirectory, String originTargetName);

}
