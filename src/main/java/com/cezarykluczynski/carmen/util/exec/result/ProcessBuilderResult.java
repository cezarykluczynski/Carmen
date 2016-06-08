package com.cezarykluczynski.carmen.util.exec.result;

import com.cezarykluczynski.carmen.util.exec.command.Command;
import com.cezarykluczynski.carmen.util.exec.command.ProcessBuilderCommand;
import com.google.common.base.Joiner;

import java.io.*;
import java.util.stream.Collectors;

public class ProcessBuilderResult extends AbstractResult implements Result {

    private ProcessBuilderCommand command;

    private String directory;

    public ProcessBuilderResult(Command command) {
        this.command = (ProcessBuilderCommand) command;
        execute();
    }

    public ProcessBuilderResult(Command command, String directory) {
        this.command = (ProcessBuilderCommand) command;
        this.directory = directory;
    }

    protected void execute() {
        ProcessBuilder pb = new ProcessBuilder(command.getCommand());
        if (directory != null) {
            pb.directory(new File(directory));
        }
        Process p;
        try {
            p = pb.start();
            int exitCode = p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            String result = Joiner.on(System.getProperty("line.separator"))
                    .join(reader.lines().collect(Collectors.toList()));
            setOutput(result);
            setSuccessful(exitCode == 0);
            if (exitCode != 0) {
                setErrorMessage(output);
            }
        } catch (IOException | InterruptedException e) {
            setSuccessful(false);
        }
    }

}
