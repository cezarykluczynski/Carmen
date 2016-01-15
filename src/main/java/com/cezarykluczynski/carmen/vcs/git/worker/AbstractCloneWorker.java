package com.cezarykluczynski.carmen.vcs.git.worker;

import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.util.exec.Command;
import com.cezarykluczynski.carmen.util.exec.Result;
import com.cezarykluczynski.carmen.util.filesystem.Directory;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractCloneWorker implements CloneWorker {

    protected abstract Result clone(Repository repositoryEntity, String cloneDirectory, String originTargetName);

    protected String buildCloneDirectory(String... directoryParts) {
        return Directory.convertPathToUnixStyleSlashes(StringUtils.join(directoryParts, "/"));
    }

}
