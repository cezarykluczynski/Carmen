package com.cezarykluczynski.carmen.vcs.git.worker;

import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.util.exec.result.Result;

public abstract class AbstractCloneWorker implements CloneWorker {

    protected abstract Result clone(Repository repositoryEntity, String cloneDirectory, String originTargetName);

}
