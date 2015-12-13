package com.cezarykluczynski.carmen.vcs.git;

import com.cezarykluczynski.carmen.util.exec.Command;
import com.cezarykluczynski.carmen.util.exec.Executor;
import com.cezarykluczynski.carmen.util.exec.Result;
import com.cezarykluczynski.carmen.vcs.git.util.GitCloneCommand;
import com.cezarykluczynski.carmen.vcs.git.util.GitCommand;

public class GitRemote {

    public static Result clone(String cloneUrl, String cloneDirectory) {
        return Executor.execute(new GitCloneCommand(cloneUrl, cloneDirectory));
    }

    public static Result renameOrigin(String repositoryDirectory, String originTargetName) {
        return Executor.execute(new Command("cd " + repositoryDirectory + " && git remote rename origin " + originTargetName));
    }

}
