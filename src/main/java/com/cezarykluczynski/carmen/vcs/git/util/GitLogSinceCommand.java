package com.cezarykluczynski.carmen.vcs.git.util;

import java.util.Date;

public class GitLogSinceCommand extends GitCommand {

    public GitLogSinceCommand(String cloneDirectory, Date since) {
        super("log --oneline");
    }

}
