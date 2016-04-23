package com.cezarykluczynski.carmen.vcs.git;

import com.cezarykluczynski.carmen.util.exec.Executor;
import com.cezarykluczynski.carmen.util.exec.Result;
import com.cezarykluczynski.carmen.vcs.git.util.GitCommand;
import com.google.common.base.Joiner;

import java.util.List;

public class GitLocal {

    public static Result diff(List<String> paths) {
        return Executor.execute(new GitCommand("diff " + Joiner.on(" ").join(paths)));
    }

}
