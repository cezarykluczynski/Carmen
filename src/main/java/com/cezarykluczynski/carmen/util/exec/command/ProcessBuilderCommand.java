package com.cezarykluczynski.carmen.util.exec.command;

import java.util.List;

public class ProcessBuilderCommand implements Command {

    private List<String> command;

    private String directory;

    public ProcessBuilderCommand(List<String> command) {
        this.command = command;
    }

    public ProcessBuilderCommand(List<String> command, String directory) {
        this.command = command;
        this.directory = directory;
    }

    public List<String> getCommand() {
        return command;
    }

    public String getDirectory() {
        return directory;
    }

}
