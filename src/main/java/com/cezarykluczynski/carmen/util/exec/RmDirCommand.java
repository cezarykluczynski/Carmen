package com.cezarykluczynski.carmen.util.exec;

import com.cezarykluczynski.carmen.util.OS;
import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand;
import com.cezarykluczynski.carmen.util.filesystem.Directory;

public class RmDirCommand extends ApacheCommonsCommand {

    public RmDirCommand(String command) {
        // TODO: it should not fail when directory does not exists
        super(wrapCommand(Directory.sanitizePathForOs(command)));
    }

    private static String wrapCommand(String command) {
        if (OS.isLinux()) {
            return "rm -rf " + command;
        } else if (OS.isWindows()) {
            return "rmdir " + command + " /s /q";
        } else {
            return "unsupported-os ";
        }
    }

}
