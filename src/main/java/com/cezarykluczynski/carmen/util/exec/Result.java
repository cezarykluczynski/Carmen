package com.cezarykluczynski.carmen.util.exec;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Result {

    private boolean successful;

    private Command command;

    private String output;

    private String errorMessage;

    private ErrorReason errorReason;

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

    private DefaultExecutor executor = new DefaultExecutor();

    public Result(Command command) {
        this.command = command;
        execute();
    }

    protected void execute() {
        CommandLine commandLine = CommandLine.parse(command.getCommand());
        executor.setStreamHandler(streamHandler);
        doExecuteCommandLine(commandLine);
    }

    protected void doExecuteCommandLine(CommandLine commandLine) {
        int exitCode;
        try {
            exitCode = executor.execute(commandLine);
            setSuccessful(exitCode == 0);
            setOutput(outputStream.toString());
        } catch(IOException e) {
            setSuccessful(false);
            setErrorMessage(e.getMessage());
            setErrorReason(ErrorReason.RUNTIME);
        }
    }

    public boolean isSuccessFul() {
        return successful;
    }

    public ErrorReason getErrorReason() {
        return errorReason;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOutput() {
        return output;
    }

    protected void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    protected void setErrorReason(ErrorReason errorReason) {
        this.errorReason = errorReason;
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected void setOutput(String output) {
        this.output = output;
    }

}
