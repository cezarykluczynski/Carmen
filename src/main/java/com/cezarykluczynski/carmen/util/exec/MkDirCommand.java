package com.cezarykluczynski.carmen.util.exec;

import com.cezarykluczynski.carmen.util.OS;
import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand;
import com.cezarykluczynski.carmen.util.filesystem.Directory;

public class MkDirCommand extends ApacheCommonsCommand {

    public MkDirCommand(String command) {
        super(wrapCommand(Directory.sanitizePathForOs(command)));
    }

    protected static String wrapCommand(String command) {
        if (OS.isLinux()) {
            return "mkdir -p " + command;
        } else if (OS.isWindows()) {
            return "mkdir " + command;
        } else {
            return "unsupported-os ";
        }
    }

}
