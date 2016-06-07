package com.cezarykluczynski.carmen.vcs.git.util;

import com.cezarykluczynski.carmen.util.DateUtil;
import com.cezarykluczynski.carmen.util.exec.command.ProcessBuilderCommand;

import java.util.Date;

public class GitLogSinceCommand extends ProcessBuilderCommand {

    public GitLogSinceCommand(String directory, Date since) {
        super(getCommandParts(since), directory);
    }

    private static String[] getCommandParts(Date since) {
        String[] commandParts = {"git",  "log",  "--pretty=%H,%ct,%ae",  "--date-order", "--reverse", "--since=" +
                DateUtil.toGitReadableDateTime(since)};
        return commandParts;
    }

}
