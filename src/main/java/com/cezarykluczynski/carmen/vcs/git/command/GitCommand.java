package com.cezarykluczynski.carmen.vcs.git.command;

import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand;

public class GitCommand extends ApacheCommonsCommand {

    public GitCommand(String command) {
        super("git " + command);
    }

}
