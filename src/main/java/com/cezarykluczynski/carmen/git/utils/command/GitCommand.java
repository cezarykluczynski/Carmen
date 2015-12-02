package com.cezarykluczynski.carmen.git.utils.command;

public class GitCommand extends Command {

    public GitCommand(String command) {
        super("git " + command);
    }

}
