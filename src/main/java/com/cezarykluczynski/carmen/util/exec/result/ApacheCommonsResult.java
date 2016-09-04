package com.cezarykluczynski.carmen.util.exec.result;

import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand;
import com.cezarykluczynski.carmen.util.exec.command.Command;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ApacheCommonsResult extends AbstractResult implements Result {

    private ApacheCommonsCommand command;

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

    private DefaultExecutor executor = new DefaultExecutor();

    public ApacheCommonsResult(Command command) {
        this.command = (ApacheCommonsCommand) command;
        execute();
    }

    protected void execute() {
        CommandLine commandLine = CommandLine.parse(command.getCommand());
        executor.setStreamHandler(streamHandler);
        doExecuteCommandLine(commandLine);
    }

    private void doExecuteCommandLine(CommandLine commandLine) {
        try {
            int exitCode = executor.execute(commandLine);
            setSuccessful(exitCode == 0);
            setOutput(outputStream.toString());
        } catch(IOException e) {
            setSuccessful(false);
            setErrorMessage(e.getMessage());
        }
    }

}
