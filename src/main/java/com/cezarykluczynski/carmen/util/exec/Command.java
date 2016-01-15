package com.cezarykluczynski.carmen.util.exec;

import com.cezarykluczynski.carmen.util.OS;

public class Command {

    private static final String windowsCommandPrefix = "cmd /c ";

    private String command;

    public Command(String command) {
        this.command = getCommandPrefix() + command;
    }

    private static String getCommandPrefix() {
        if (OS.isWindows()) {
            return windowsCommandPrefix;
        }

        return "";
    }

    public String getCommand() {
        return command;
    }

}
