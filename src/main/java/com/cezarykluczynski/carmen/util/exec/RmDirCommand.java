package com.cezarykluczynski.carmen.util.exec;

import com.cezarykluczynski.carmen.util.OS;
import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand;

public class RmDirCommand extends ApacheCommonsCommand {

    public RmDirCommand(String command) {
        super(wrapCommand(command));
    }

    protected static String wrapCommand(String command) {
        if (OS.isLinux()) {
            return "rm -rf " + command;
        } else if (OS.isWindows()) {
            return "rmdir " + command + " /s /q";
        } else {
            return "unsupported-os ";
        }
    }

}
