package com.cezarykluczynski.carmen.vcs.git.util;

import com.cezarykluczynski.carmen.util.exec.Command;

import java.util.Date;

public class GitLogSinceCommand extends Command {

    public GitLogSinceCommand(String directory, Date since) {
        super("pushd " + directory);
    }

}
