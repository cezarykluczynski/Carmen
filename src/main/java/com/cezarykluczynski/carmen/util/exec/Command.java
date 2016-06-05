package com.cezarykluczynski.carmen.util.exec;

import com.cezarykluczynski.carmen.util.OS;

public class Command {

    private static final String WINDOWS_COMMAND_PREFIX = "cmd /c ";

    private String command;

    public Command(String command) {
        this.command = getCommandPrefix() + command;
    }

    private static String getCommandPrefix() {
        if (OS.isWindows()) {
            return WINDOWS_COMMAND_PREFIX;
        }

        return "";
    }

    public String getCommand() {
        return command;
    }

}
