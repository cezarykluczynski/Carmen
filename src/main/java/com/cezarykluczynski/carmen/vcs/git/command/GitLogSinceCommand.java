package com.cezarykluczynski.carmen.vcs.git.command;

import com.cezarykluczynski.carmen.util.DateUtil;
import com.cezarykluczynski.carmen.util.exec.command.ProcessBuilderCommand;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

public class GitLogSinceCommand extends ProcessBuilderCommand {

    public GitLogSinceCommand(String directory, Date since, Date before) {
        super(getCommandParts(since, before), directory);
    }

    private static String[] getCommandParts(Date since, Date before) {
        List<String> commandParts = Lists.newArrayList("git", "log", "--pretty=%H,%ct,%ae", "--date-order",
            "--reverse");

        if (since != null) {
            commandParts.add("--since=" + DateUtil.toGitReadableDateTime(since));
        }

        if (before != null) {
            commandParts.add("--before=" + DateUtil.toGitReadableDateTime(before));
        }

        String[] commandPartsArray = new String[commandParts.size()];
        commandPartsArray = commandParts.toArray(commandPartsArray);

        return commandPartsArray;
    }

}
