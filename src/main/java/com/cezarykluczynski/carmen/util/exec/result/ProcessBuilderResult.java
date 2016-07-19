package com.cezarykluczynski.carmen.util.exec.result;

import com.cezarykluczynski.carmen.util.exec.command.Command;
import com.cezarykluczynski.carmen.util.exec.command.ProcessBuilderCommand;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.stream.Collectors;

public class ProcessBuilderResult extends AbstractResult implements Result {

    private ProcessBuilderCommand command;

    public ProcessBuilderResult(Command command) {
        this.command = (ProcessBuilderCommand) command;
        execute();
    }

    protected void execute() {
        ProcessBuilder pb = new ProcessBuilder(command.getCommand());
        pb.redirectOutput();
        if (command.getDirectory() != null) {
            pb.directory(new File(command.getDirectory()));
        }
        Process p;
        try {
            p = pb.start();
            Pair<String, Integer> result = getResult(p);
            setOutput(result.getLeft());
            Integer exitCode = result.getRight();
            setSuccessful(exitCode == 0);
            if (exitCode != 0) {
                setErrorMessage(output);
            }
        } catch (IOException e) {
            setSuccessful(false);
        }
    }

    private Pair<String, Integer> getResult(Process p) {
        try {
            InputStream inputStream = p.getInputStream();
            InputStream errorStream = p.getErrorStream();
            OutputStream outputStream = p.getOutputStream();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream));
            String result = Joiner.on(System.getProperty("line.separator"))
                    .join(reader.lines().collect(Collectors.toList()));
            inputStream.close();
            errorStream.close();
            outputStream.close();
            Integer exitCode = p.waitFor();
            return Pair.of(result, exitCode);
        } catch(IOException | InterruptedException e) {
            return Pair.of(null, null);
        }
    }

}
