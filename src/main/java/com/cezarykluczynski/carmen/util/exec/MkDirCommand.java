package com.cezarykluczynski.carmen.util.exec;

import com.cezarykluczynski.carmen.util.OS;
import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand;

import static com.cezarykluczynski.carmen.util.filesystem.Directory.sanitizePathForOs;

public class MkDirCommand extends ApacheCommonsCommand {

    public MkDirCommand(String command) {
        super(wrapCommand(command));
    }

    private static String wrapCommand(String command) {
        command = sanitizePathForOs(command);
        // TODO: it should not fail if directory exists
        if (OS.isLinux()) {
            return "mkdir -p " + command;
        } else if (OS.isWindows()) {
            return "mkdir " + command;
        } else {
            return "unsupported-os ";
        }
    }

}
