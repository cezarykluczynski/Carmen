package com.cezarykluczynski.carmen.vcs.git;

import com.cezarykluczynski.carmen.util.exec.executor.Executor;
import com.cezarykluczynski.carmen.util.exec.result.Result;
import com.cezarykluczynski.carmen.vcs.git.command.GitCloneCommand;

public class GitRemote {

    public static Result clone(String cloneUrl, String cloneDirectory, String originTargetName) {
        return Executor.execute(new GitCloneCommand(cloneUrl, cloneDirectory, originTargetName));
    }

}
