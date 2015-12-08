package com.cezarykluczynski.carmen.util.exec;

import com.cezarykluczynski.carmen.util.OS;

public class MkDirCommand extends Command {

    public MkDirCommand(String command) {
        super(wrapCommand(command));
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
