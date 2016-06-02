package com.cezarykluczynski.carmen.vcs.git.util;

import com.cezarykluczynski.carmen.util.DateUtil;

import java.util.Date;

public class GitLogSinceCommand extends GitCommand {

    public GitLogSinceCommand(String directory, Date since) {
        super("log --pretty=%H,%ct,%ae --date-order --reverse --since=" + DateUtil.toGitReadableDateTime(since));
    }

}
