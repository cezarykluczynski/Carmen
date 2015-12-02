package com.cezarykluczynski.carmen.git.utils.command;

public class Command {

    private final String commandPrefix = "cmd /c";

    private String command;

    public Command(String command) {
        this.command = commandPrefix + command;
    }

    public String getCommand() {
        return command;
    }

}
