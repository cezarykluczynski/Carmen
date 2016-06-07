package com.cezarykluczynski.carmen.util.exec.command;

public class ProcessBuilderCommand implements Command {

    private String[] command;

    private String directory;

    public ProcessBuilderCommand(String[] command) {
        this.command = command;
    }

    public ProcessBuilderCommand(String[] command, String directory) {
        this.command = command;
        this.directory = directory;
    }

    public String[] getCommand() {
        return command;
    }

    public String getDirectory() {
        return directory;
    }

}
