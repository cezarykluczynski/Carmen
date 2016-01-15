package com.cezarykluczynski.carmen.vcs.git.util;

public class GitCloneCommand extends GitCommand {

    public GitCloneCommand(String cloneUrl, String cloneDirectory, String originTargetName) {
        super("clone -o " + originTargetName + " " + cloneUrl + " " + cloneDirectory);
    }

}
