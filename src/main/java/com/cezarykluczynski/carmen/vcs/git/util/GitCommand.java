package com.cezarykluczynski.carmen.vcs.git.util;

import com.cezarykluczynski.carmen.util.exec.Command;

public class GitCommand extends Command {

    public GitCommand(String command) {
        super("git " + command);
    }

}
